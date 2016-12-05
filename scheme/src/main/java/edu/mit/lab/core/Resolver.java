package edu.mit.lab.core;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import edu.mit.lab.constant.Scheme;
import edu.mit.lab.infts.IRelevance;
import edu.mit.lab.infts.idao.IDAOForDBScheme;
import edu.mit.lab.meta.Keys;
import edu.mit.lab.meta.Tables;
import edu.mit.lab.skeleton.DAOFactory;
import edu.mit.lab.skeleton.factory.IDAOFactory;
import edu.mit.lab.utils.Toolkit;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.GraphFactory;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * <p>Title: Kewill Lab Center</p>
 * <p>Description: edu.mit.lab.core.Resolver</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Kewill Co., Ltd.</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 11/14/2016
 */
public class Resolver {

    private static final int HEIGHT_THRESHOLD = 3;
    private static final String SCRIPT_FILE_NAME = "clear_data_script.sql";
    private static final String GRAPH_FILE_NAME_PREFIX = "stream_graph_";
    private static final String GRAPH_FILE_NAME_SUFFIX = ".dgs";
    private static final int FIXED_ROW_COUNT = 50;

    private int viewClosedCounter;
    private SortedSet<String> rootNodeIds;
    private List<String> tableIds;
    private StringBuilder script;

    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    private void init() {
        viewClosedCounter = 0;
        tableIds = new LinkedList<>();
        script = new StringBuilder(Scheme.PREVENT_EDITOR_ADD_BOM_HEADER);
    }

    public Resolver() {
        init();
    }

    private void setRootNodeIds(SortedSet<String> rootNodeIds) {
        this.rootNodeIds = rootNodeIds;
    }

    private static Connection createConnection() {
        Connection connection = null;
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        assert url != null;
        HikariConfig config = new HikariConfig(url.getPath() + Scheme.HIKARI_PROPERTIES);
        HikariDataSource dataSource = new HikariDataSource(config);
        try {
            connection = dataSource.getConnection();
            System.out.println(String.format("Database connect success! %s", connection.toString()));
        } catch (SQLException e) {
            System.err.println(String.format("Database connect failed! %s", "Ops"));
            System.err.println(e.getMessage());
        }

        return connection;
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        Resolver resolver = new Resolver();
        List<IRelevance<String, List<String>>> lstFKRef = null;
        try (Connection connection = createConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            resolver.processEntityType(metaData);

            List<Tables> lstTable = resolver.processTable(connection, metaData);
            resolver.collect(lstTable);
//            processPKRef(connection, lstTable);
            lstFKRef = resolver.processFKRef(connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
//        List<Tree<String>> trees = buildTableTree(lstFKRef);
        Graph overview = resolver.overview(lstFKRef);
        resolver.setRootNodeIds(Toolkit.resolveDisconnectedGraph(overview));

        ExecutorService executor = Executors.newFixedThreadPool(10);
        Future<Integer> status = executor.submit(resolver.genSQLScript(overview));
        Future<List<Graph>> graphs = executor.submit(resolver.graphs(overview));

        try {
            System.out
                .println(status.get() == 0 ? "Success to generate SQL script!" : "Failed to generate SQL script!");
            List<Graph> lstGraph = graphs.get();
            resolver.viewClosedCounter = lstGraph.size();
            lstGraph.forEach(
                graph -> {
                    executor.execute(resolver.persist(graph));

                    executor.execute(resolver.display(graph));
                }
            );
        } catch (ExecutionException e) {
            System.err.println(e.getMessage());
        } finally {
            executor.shutdown();
        }
        long end = System.currentTimeMillis();
        long duration = end - start;
        System.out.println(String
            .format(Scheme.TIME_DURATION_PROCESS, TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES
                    .toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))));
    }

    private Runnable display(final Graph graph) {
        return () -> {
            Viewer viewer = graph.display();
            ViewerPipe pipe = viewer.newViewerPipe();
            pipe.addViewerListener(listener(graph));
            //Refer to https://github.com/graphstream/gs-core/issues/209
            pipe.addAttributeSink(graph);
            boolean loop = true;
            while (loop) {
                pipe.pump();
                loop = !graph.hasAttribute(Scheme.UI_VIEW_CLOSED);
                if (viewClosedCounter == 0) {
                    viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
                    viewer.close();
                }
            }
        };
    }

    private Runnable persist(Graph graph) {
        return () -> {
            try {
                graph.write(Scheme.WORK_DIR + GRAPH_FILE_NAME_PREFIX + graph.getId() + GRAPH_FILE_NAME_SUFFIX);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        };
    }

    private void collect(List<Tables> lstTable) {
        lstTable.forEach(table -> tableIds.add(table.getTableName()));
    }

    /**
     * This method must be called after method <code>collect</code>
     */
    private StringBuilder genAuditLogScript() {
        StringBuilder complement = new StringBuilder(Scheme.STD_SQL_COMMENTS_BOILERPLATE_FOR_AUDIT_LOG_TABLES);
        tableIds.forEach(item -> {
            boolean exclusive = Toolkit.INSTANCE.matcher(item).matches();
            complement.append(String
                .format(
                    (exclusive ? "--" : "") + Scheme.STD_SQL_DELETE_STATEMENT,
                    StringUtils.lowerCase(item) + Scheme.AUDIT_LOG_TABLE_POSTFIX));
        });
        return complement;
    }

    private Graph overview(List<IRelevance<String, List<String>>> lstFKRef) {
        //Remember processed tables position which corresponding to position in the list of nodes
        Graph overview = new GraphFactory().newInstance(Scheme.DEPENDENCY, SingleGraph.class.getName());
        if (!CollectionUtils.isEmpty(lstFKRef)) {
            lstFKRef.forEach(item -> build(overview, item));
        }
        return overview;
    }

    private Callable<Integer> genSQLScript(final Graph graph) {
        return () -> {
            Set<String> untouched = new TreeSet<>(tableIds);
            rootNodeIds.forEach(
                rootId -> {
                    Node root = graph.getNode(rootId);
                    script.append(String.format(
                        Scheme.STD_SQL_COMMENTS_BOILERPLATE,
                        StringUtils.removeFirst(rootId.toLowerCase(), Scheme.NODE_PREFIX)));
                    Toolkit.postTraverse(graph, root, untouched, script);
                }
            );

            script.append(Scheme.STD_SQL_COMMENTS_BOILERPLATE_FOR_INDEPENDENT_TABLES);
            untouched.forEach(item -> {
                boolean exclusive = Toolkit.INSTANCE.matcher(item).matches();
                script.append(
                    String.format(
                        (exclusive ? "--" : "") + Scheme.STD_SQL_DELETE_STATEMENT,
                        StringUtils.lowerCase(item)));
            });

            script.append(genAuditLogScript());

            script.append(Scheme.STD_SQL_COMMIT);
            try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(Scheme.WORK_DIR + SCRIPT_FILE_NAME, false),
                Charset.forName(Scheme.UTF_8).newEncoder())) {
                writer.write(script.toString());
                writer.flush();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
            return 0;
        };
    }

    private void build(Graph graph, IRelevance<String, List<String>> item) {
        String targetNodeId = Scheme.NODE_PREFIX + item.to();
        String sourceNodeId = Scheme.NODE_PREFIX + item.from();
        String linkEdgeId = Scheme.EDGE_PREFIX + item.to() + item.from();
        Node target, source;
        int status = present(graph, item);
        switch (status) {
            case Scheme.BOTH_NODES_ABSENT:
                target = graph.addNode(targetNodeId);
                source = graph.addNode(sourceNodeId);
                break;
            case Scheme.ONLY_TARGET_NODE_PRESENTS:
                target = graph.getNode(targetNodeId);
                source = graph.addNode(sourceNodeId);
                break;
            case Scheme.ONLY_SOURCE_NODE_PRESENTS:
                target = graph.addNode(targetNodeId);
                source = graph.getNode(sourceNodeId);
                break;
            default:
                target = graph.getNode(targetNodeId);
                source = graph.getNode(sourceNodeId);
        }
        Toolkit.addNodeInfo(item, target, source);
        Edge edge = graph.addEdge(linkEdgeId, sourceNodeId, targetNodeId, true);
        Toolkit.addEdgeInfo(item, edge);
    }

    private Callable<List<Graph>> graphs(final Graph graph) {
        final List<Graph> graphs = new ArrayList<>();
        return () -> {
            rootNodeIds.forEach(rootId -> {
                Node root = graph.getNode(rootId);
                if (Toolkit.height(root) >= HEIGHT_THRESHOLD) graph(graphs, root, rootId);
            });
            return graphs;
        };
    }

    private void graph(List<Graph> graphs, Node root, String rootId) {
        Graph result = new SingleGraph(StringUtils.remove(rootId, Scheme.NODE_PREFIX));
        result.addAttribute(Scheme.UI_QUALITY);
        result.addAttribute(Scheme.UI_ANTIALIAS);
        root.getBreadthFirstIterator(false)
            .forEachRemaining(currentNode -> currentNode.getEnteringEdgeSet().forEach(
                edge -> {
                    Node source = edge.getSourceNode();
                    Node target = edge.getTargetNode();
                    if (result.getNode(target.getId()) == null) {
                        Node parent = result.addNode(target.getId());
                        target.getAttributeKeySet()
                            .forEach(key -> parent.addAttribute(key, target.<String>getAttribute(key)));
                    }
                    if (result.getNode(source.getId()) == null) {
                        Node child = result.addNode(source.getId());
                        source.getAttributeKeySet()
                            .forEach(key -> child.addAttribute(key, source.<String>getAttribute(key)));
                    }
                    result.addEdge(edge.getId(), source.getId(), target.getId(), true);
                    edge.getAttributeKeySet()
                        .forEach(key -> result.getEdge(edge.getId())
                            .addAttribute(key, edge.<String>getAttribute(key)));
                }
            ));

        result.addAttribute(Scheme.UI_DEFAULT_TITLE, StringUtils.remove(rootId, Scheme.NODE_PREFIX));
        result.addAttribute(Scheme.UI_STYLESHEET, "url(css/polish.css)");
        graphs.add(result);
    }

    private int present(Graph graph, IRelevance<String, List<String>> item) {
        String targetId = Scheme.NODE_PREFIX + item.to();
        String sourceId = Scheme.NODE_PREFIX + item.from();
        int target = graph.getNode(targetId) != null ? Scheme.ONLY_TARGET_NODE_PRESENTS : 0;
        int source = graph.getNode(sourceId) != null ? Scheme.ONLY_SOURCE_NODE_PRESENTS : 0;
        return target | source;
    }

    private List<String> processEntityType(DatabaseMetaData metaData) throws SQLException {
        List<String> lstEntityTypes = new ArrayList<>();
        ResultSet types = metaData.getTableTypes();
        while (types.next()) {
            lstEntityTypes.add(types.getString(Scheme._TABLE_TYPE));
        }
        lstEntityTypes.forEach(System.out::println);
        return lstEntityTypes;
    }

    private List<Tables> processTable(Connection connection, DatabaseMetaData metaData) {
        List<Tables> lstTable = new ArrayList<>();
        try (ResultSet tables =
                 metaData.getTables(connection.getCatalog(), connection.getSchema(), null,
                     new String[]{Scheme.ENTITY_TABLE})) {
            ResultSetMetaData meta = tables.getMetaData();
            int columns = meta.getColumnCount();
            Set<String> nameSet = new HashSet<>(columns);
            for (int i = 1; i < columns; i++) {
                nameSet.add(meta.getColumnName(i));
            }

            while (tables.next()) {
                Tables tableMeta = new Tables();
                pushTableSummaryInfo(tables, lstTable, nameSet, tableMeta);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~#Tables information list#~~~~~~~~~~~~~~~~~~~~~~~");
        lstTable.forEach(System.out::println);
        return lstTable;
    }

    @SuppressWarnings(value = {"unused"})
    private List<IRelevance<String, List<String>>> processPKRef(final Connection connection) {
        List<IRelevance<String, List<String>>> lstPrimaryKey = pushPrimaryKeyInfo(connection);
        System.out.println(
            "~~~~~~~~~~~~~~~~~~~~~~~#Tables primary keys reference information list#~~~~~~~~~~~~~~~~~~~~~~~");
        lstPrimaryKey.forEach(System.out::println);
        return lstPrimaryKey;
    }

    private List<IRelevance<String, List<String>>> processFKRef(final Connection connection) {
        List<IRelevance<String, List<String>>> lstForeignKey = pushForeignKeyInfo(connection);
        System.out.println(
            "~~~~~~~~~~~~~~~~~~~~~~~#Tables foreign keys reference information list#~~~~~~~~~~~~~~~~~~~~~~~");
        lstForeignKey.forEach(System.out::println);
        return lstForeignKey;
    }

    private List<IRelevance<String, List<String>>> pushForeignKeyInfo(Connection connection) {
        return processKeysInfo(connection, true);
    }

    private void handleRefKeys(
        ResultSet result, List<IRelevance<String, List<String>>> lstRefKeys, Map<String, Integer> mapIds)
        throws SQLException {
        while (result.next()) {
            String pkTableName = result.getString(Scheme.PKTABLE_NAME);
            String fkTableName = result.getString(Scheme.FKTABLE_NAME);
            String fkColumnName = result.getString(Scheme.FKCOLUMN_NAME);
            String symbol = String.format("<%s>$<%s>", pkTableName, fkTableName);
            Integer pos = MapUtils.getInteger(mapIds, symbol);
            if (pos == null) {
                Keys foreignKeyInfo = new Keys();
                foreignKeyInfo.setPkTableName(pkTableName);
                foreignKeyInfo.addPkColumnName(result.getString(Scheme.PKCOLUMN_NAME));
                foreignKeyInfo.setFkTableName(fkTableName);
                foreignKeyInfo.addFkColumnName(fkColumnName);
                foreignKeyInfo.setKeySequence(result.getShort(Scheme.KEY_SEQ));
                lstRefKeys.add(foreignKeyInfo);
                mapIds.put(symbol, lstRefKeys.size() - 1);
            } else {
                IRelevance<String, List<String>> reference = lstRefKeys.get(pos);
                reference.set(fkColumnName);
            }
        }
    }

    private List<IRelevance<String, List<String>>> pushPrimaryKeyInfo(Connection connection) {
        return processKeysInfo(connection, false);
    }

    private List<IRelevance<String, List<String>>> processKeysInfo(Connection connection, boolean forExportKeys) {
        List<IRelevance<String, List<String>>> lstRefKeys = new ArrayList<>();
        try {
            IDAOFactory factory = new DAOFactory();
            String productName = connection.getMetaData().getDatabaseProductName();
            IDAOForDBScheme daoMeta = factory.createDBScheme(productName);
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                forExportKeys ? daoMeta.fkConstraint() : daoMeta.pkConstraint())) {
                int times = 0;
                boolean hasNext = true;
                Map<String, Integer> identifiers = new WeakHashMap<>();
                do {
                    preparedStatement.setString(1, connection.getSchema());
                    int start, end;
                    switch (productName) {
                        case Scheme.DB_TYPE_ORACLE:
                            start = times * FIXED_ROW_COUNT;
                            end = (++times) * FIXED_ROW_COUNT;
                            break;
                        case Scheme.DB_TYPE_MYSQL:
                            start = (times++) * FIXED_ROW_COUNT + 1;
                            end = FIXED_ROW_COUNT;
                            break;
                        default:
                            start = 0;
                            end = 0;
                    }
                    preparedStatement.setInt(2, start);
                    preparedStatement.setInt(3, end);
                    try (ResultSet refKeyResult = preparedStatement.executeQuery()) {
                        refKeyResult.setFetchDirection(ResultSet.TYPE_FORWARD_ONLY);
                        if (hasNext = refKeyResult.isBeforeFirst()) {
                            handleRefKeys(refKeyResult, lstRefKeys, identifiers);
                        }
                    } catch (SQLException e) {
                        System.err.println(e.getMessage());
                    }
                } while (hasNext);
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return lstRefKeys;
    }

    private static void pushTableSummaryInfo(
        ResultSet tables, List<Tables> lstTable, Set<String> nameSet, Tables tableMeta) throws SQLException {

        if (nameSet.contains(Scheme.TABLE_CAT)) {
            tableMeta.setTableCatalog(tables.getString(Scheme.TABLE_CAT));
        }
        if (nameSet.contains(Scheme.TABLE_SCHEM)) {
            tableMeta.setTableSchema(tables.getString(Scheme.TABLE_SCHEM));
        }
        if (nameSet.contains(Scheme._TABLE_NAME)) {
            tableMeta.setTableName(tables.getString(Scheme._TABLE_NAME));
        }
        if (nameSet.contains(Scheme._TABLE_TYPE)) {
            tableMeta.setTableType(tables.getString(Scheme._TABLE_TYPE));
        }
        if (nameSet.contains(Scheme.REMARKS)) {
            tableMeta.setTableRemark(tables.getString(Scheme.REMARKS));
        }
        if (nameSet.contains(Scheme.TYPE_CAT)) {
            tableMeta.setTypeCatalog(tables.getString(Scheme.TYPE_CAT));
        }
        if (nameSet.contains(Scheme.TYPE_SCHEM)) {
            tableMeta.setTypeSchema(tables.getString(Scheme.TYPE_SCHEM));
        }
        if (nameSet.contains(Scheme._TYPE_NAME)) {
            tableMeta.setTypeName(tables.getString(Scheme._TYPE_NAME));
        }
        if (nameSet.contains(Scheme.SELF_REFERENCING_COL_NAME)) {
            tableMeta.setTableSelfRefColName(tables.getString(Scheme.SELF_REFERENCING_COL_NAME));
        }
        if (nameSet.contains(Scheme.REF_GENERATION)) {
            tableMeta.setTableRefGeneration(tables.getString(Scheme.REF_GENERATION));
        }
        lstTable.add(tableMeta);
    }

    private ViewerListener listener(Graph graph) {
        return new ViewerListener() {
            @Override
            public void viewClosed(String viewName) {
                --viewClosedCounter;
                System.out.println(String.format("Graphs[%s] ara closed!", graph.getId()));
            }

            @Override
            public void buttonPushed(String id) {
                Node node = graph.getNode(id);
                if (node != null) {
                    for (Edge edge : node.getEachLeavingEdge()) {
                        edge.setAttribute(Scheme.TEXT_MODE, Scheme.SHOW_TIPS);
                    }
                }
            }

            @Override
            public void buttonReleased(String id) {
                Node node = graph.getNode(id);
                if (node != null) {
                    for (Edge edge : node.getEachLeavingEdge()) {
                        edge.setAttribute(Scheme.TEXT_MODE, Scheme.HIDE_TIPS);
                    }
                }
            }
        };
    }
}

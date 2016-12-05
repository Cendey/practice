package edu.mit.lab.repos.common;

import edu.mit.lab.infts.idao.IDAOForMeta;

/**
 * <p>Title: MIT Lib Project</p>
 * <p>Description: edu.mit.lab.repos.common.DAOForMeta</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Lib Co., Ltd</p>
 *
 * @author <chao.deng@mit.edu>
 * @version 1.0
 * @since 12/5/2016
 */
public abstract class DAOForMeta implements IDAOForMeta {

    public abstract String exportedKeys();

    public abstract String importedKeys();

    public abstract String primaryKeys();
}

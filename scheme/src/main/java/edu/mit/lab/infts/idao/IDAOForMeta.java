package edu.mit.lab.infts.idao;

/**
 * <p>Title: MIT Lib Project</p>
 * <p>Description: edu.mit.lab.infts.idao.IDAOForMeta</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Lib Co., Ltd</p>
 *
 * @author <chao.deng@mit.edu>
 * @version 1.0
 * @since 12/5/2016
 */
public interface IDAOForMeta {

    String exportedKeys();

    String importedKeys();

    String primaryKeys();
}

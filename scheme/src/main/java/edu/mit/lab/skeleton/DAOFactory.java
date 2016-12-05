package edu.mit.lab.skeleton;

import edu.mit.lab.constant.Scheme;
import edu.mit.lab.infts.idao.IDAOForDBScheme;
import edu.mit.lab.skeleton.factory.IDAOFactory;

/**
 * <p>Title: MIT Lib Project</p>
 * <p>Description: edu.mit.lab.skeleton.DAOFactory</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Lib Co., Ltd</p>
 *
 * @author <chao.deng@mit.edu>
 * @version 1.0
 * @since 12/5/2016
 */
public class DAOFactory extends IDAOFactory {

    @Override
    public IDAOForDBScheme createDBScheme(String dbType) {
        IDAOForDBScheme instance;
        switch (dbType) {
            case Scheme.DB_TYPE_ORACLE:
                instance = new edu.mit.lab.repos.oracle.DAOForDBScheme();
                break;
            case Scheme.DB_TYPE_MYSQL:
                instance = new edu.mit.lab.repos.mysql.DAOForDBScheme();
                break;
            default:
                instance = null;
        }
        return instance;
    }
}

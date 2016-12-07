package edu.mit.lab.skeleton;

import edu.mit.lab.constant.Scheme;
import edu.mit.lab.exception.UnIdentifiedException;
import edu.mit.lab.infts.idao.IDAOForMeta;
import edu.mit.lab.repos.oracle.DAOForMeta;
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
    public IDAOForMeta createDBScheme(String dbType) throws UnIdentifiedException {
        IDAOForMeta instance;
        switch (dbType) {
            case Scheme.DB_TYPE_ORACLE:
                instance = new DAOForMeta();
                break;
            case Scheme.DB_TYPE_MYSQL:
                instance = new edu.mit.lab.repos.mysql.DAOForMeta();
                break;
            default:
                throw new UnIdentifiedException(dbType);
        }
        return instance;
    }
}

package edu.mit.lab.skeleton.factory;

import edu.mit.lab.exception.UnIdentifiedException;
import edu.mit.lab.infts.idao.IDAOForMeta;

/**
 * <p>Title: MIT Lib Project</p>
 * <p>Description: edu.mit.lab.skeleton.factory.IDAOFactory</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Lib Co., Ltd</p>
 *
 * @author <chao.deng@mit.edu>
 * @version 1.0
 * @since 12/5/2016
 */
public abstract class IDAOFactory {

    public abstract IDAOForMeta createDBScheme(String dbType) throws UnIdentifiedException;
}

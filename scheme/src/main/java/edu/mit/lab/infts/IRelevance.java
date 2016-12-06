package edu.mit.lab.infts;

import java.io.Serializable;

/**
 * <p>Title: MIT Lib Project</p>
 * <p>Description: edu.mit.lab.infts.IRelevance</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Lib Co., Ltd</p>
 *
 * @author <chao.deng@mit.edu>
 * @version 1.0
 * @since 11/29/2016
 */
public interface IRelevance<T, V> extends Serializable{

    T from();

    T to();

    V get(T name);

    void set(T item);
}

package com.pisx.fileupload.dao;

import java.io.Serializable;

/**
 * Created by pisx on 2014/12/23.
 */
public interface Parameter extends Iterable<Object>,Serializable {

    /**
     * 判断该参数是不是复合参数，复合参数在进行再复合时需要用括号包围
     *
     * @return	是否复合数
     */
    boolean isCombined();

    /**
     * 把参数列表转换为WHERE子句中的条件
     *
     * @return	WHERE子句中的条件
     */
    public String toQuery();

    /**
     * 转化为WHERE子句中的查询参数，仍然返回传入的StringBuffer对象，以方便链式操作
     *
     * @param	sb	存放转化结果的缓冲区
     * @return	传入的StringBuffer对象
     */
    StringBuffer toQuery(StringBuffer sb);

}

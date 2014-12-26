package com.pisx.fileupload.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pisx on 2014/12/23.
 */
public class ParameterList implements Parameter {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 参数之间的逻辑关系
     */
    public enum Logic {

        OR(" or "), AND(" and ");

        /**
         * 运算符号
         */
        private String m_sOperate;

        /**
         * 构造方法
         *
         * @param	operate	运算符号
         */
        Logic(String operate) {
            m_sOperate = operate;
        }

        /**
         * 返回实际的运算符
         */
        public String getOperate() {
            return m_sOperate;
        }

    }

    /**
     * 参数列表
     */
    private List<Parameter> m_oParameters = new ArrayList<Parameter>();

    /**
     * 和参数列表一一对应的逻辑关系列表
     */
    private List<Logic> m_oParaLogics = new ArrayList<Logic>();

    /**
     * 构造方法
     */
    public ParameterList() {
    }

    /* (non-Javadoc)
     * @see com.trs.blog.dao.Parameter#isCombined()
     */
    public boolean isCombined() {
        return m_oParameters.size() > 1;
    }

    /* (non-Javadoc)
     * @see com.trs.blog.dao.Parameter#toQuery()
     */
    public String toQuery() {
        return toQuery(new StringBuffer(m_oParaLogics.size() * 32)).toString();
    }

    /**
     * 返回已经直接加入的参数的数量
     *
     * @return	直接加入的参数的数量
     */
    public int size() {
        return m_oParaLogics.size();
    }

    /* (non-Javadoc)
     * @see com.trs.blog.dao.Parameter#toQuery(java.lang.StringBuffer)
     */
    public StringBuffer toQuery(StringBuffer sb) {
        int length = m_oParameters.size();
        for (int i = 0; i < length; i++) {
            Parameter p = m_oParameters.get(i);
            Logic l = m_oParaLogics.get(i);
            if (i > 0) {
                sb.append(l.getOperate());
            }
            if (p.isCombined()) {
                sb.append(" (");
                p.toQuery(sb);
                sb.append(") ");
            } else {
                p.toQuery(sb);
            }
        }
        return sb;
    }

    public void addParameter(Parameter p) {
        addParameter(p, Logic.AND);
    }

    public void addParameter(Parameter p, Logic l) {
        if (p == null) {
            throw new IllegalArgumentException("Parameter is null");
        }
        if (l == null) {
            throw new IllegalArgumentException("Logic is null");
        }
        if (p == this) {
            throw new IllegalArgumentException("Parameter is this");
        }
        m_oParameters.add(p);
        m_oParaLogics.add(l);
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<Object> iterator() {
        return new Iterator<Object>() {

            int m_iIndex = 0;

            Object m_oObject = null;

            Iterator<Object> m_oIterator = null;

            /* (non-Javadoc)
             * @see java.util.Iterator#hasNext()
             */
            public boolean hasNext() {
                if (m_oObject != null || findNext()) {
                    return true;
                }
                return false;
            }

            /* (non-Javadoc)
             * @see java.util.Iterator#next()
             */
            public Object next() {
                if (m_oObject != null || findNext()) {
                    Object obj = m_oObject;
                    m_oObject = null;
                    return obj;
                }
                return null;
            }

            private boolean findNext() {
                if (m_iIndex == -1) {
                    return false;
                }

                if (m_oIterator != null && m_oIterator.hasNext()) {
                    m_oObject = m_oIterator.next();
                    return true;
                }

                while (m_iIndex < m_oParameters.size()) {
                    Parameter parameter = (Parameter) m_oParameters.get(m_iIndex++);
                    if (parameter != null) {
                        if (parameter instanceof Iterable<?>) {
                            m_oIterator = ((Iterable<Object>) parameter).iterator();
                            if (m_oIterator != null) {
                                return findNext();
                            }
                        }
                        if (parameter instanceof ParameterPair) {
                            m_oObject = ((ParameterPair) parameter).getValue();
                            if (m_oObject != null) {
                                return true;
                            }
                        }
                    }
                }

                m_iIndex = -1;
                return false;
            }

            /* (non-Javadoc)
             * @see java.util.Iterator#remove()
             */
            public void remove() {
                throw new RuntimeException("Not supported method");
            }

        };
    }

    public void clear() {
        m_oParameters.clear();
        m_oParaLogics.clear();
    }
}

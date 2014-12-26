package com.pisx.fileupload.dao;

import java.util.Iterator;

/**
 * Created by pisx on 2014/12/23.
 */
public class ParameterPair implements Parameter {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 查询运算符号
     */
    public enum Operate {

        /**
         * 从左往右依次为：等于，
         */
        EQUAL(" = "), ISNULL(" is null "), NOTNULL(" is not null "), LIKE(" like "), BETWEEN(" between "), LESS(" < "),
        LESSEQUAL(" <= "), GREAT(" > "), GREATEQUAL(" >= "), NOTEQUAL(" != ");

        /**
         * 运算符号
         */
        private String m_sOperate;

        /**
         * 构造方法
         *
         * @param    operate    运算符号
         */
        Operate(String operate) {
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
     * 运算符
     */
    private Operate m_oOperate;

    /**
     * 字段名称
     */
    private String m_sField;

    /**
     * 字段值１（开始值）
     */
    private Object m_oBegin;

    /**
     * 字段值２（结束值）
     */
    private Object m_oClose;

    /**
     * 构造方法
     *
     * @param    field    字段名
     * @param    value    字段值
     */
    public ParameterPair(String field, boolean value) {
        this(field, Operate.EQUAL, Boolean.valueOf(value), null);
    }

    /**
     * 构造方法
     *
     * @param    field    字段名
     * @param    value    字段值
     */
    public ParameterPair(String field, int value) {
        this(field, Operate.EQUAL, Integer.valueOf(value), null);
    }

    /**
     * 构造方法
     *
     * @param    field    字段名
     * @param    value    字段值
     */
    public ParameterPair(String field, Object value) {
        this(field, Operate.EQUAL, value, null);
    }

    /**
     * 构造方法
     *
     * @param    field    字段名
     * @param    operate    运算符
     */
    public ParameterPair(String field, Operate operate) {
        this(field, operate, null, null);
    }

    /**
     * 构造方法
     *
     * @param    field    字段名
     * @param    operate    运算符
     * @param    value    字段值
     */
    public ParameterPair(String field, Operate operate, Object value) {
        this(field, operate, value, null);
    }

    /**
     * 构造方法
     *
     * @param    field    字段名
     * @param    operate    运算符
     * @param    begin    范围的起始值
     * @param    close    范围的结束值
     */
    public ParameterPair(String field, Operate operate, Object begin, Object close) {
        m_sField = field;
        m_oOperate = operate;
        if (operate == Operate.LIKE) {// && begin instanceof String 是多余的
            m_oBegin = "%" + begin + "%";
        } else {
            m_oBegin = begin;
        }
        m_oClose = close;
    }

    /**
     * 返回运算符
     *
     * @return 运算符
     */
    public Operate getOperate() {
        return m_oOperate;
    }

    /**
     * 设置运算符
     *
     * @param    operate    运算符
     */
    public void setOperate(Operate operate) {
        m_oOperate = operate;
    }

    /**
     * 返回字段名称
     *
     * @return 字段名称
     */
    public String getField() {
        return m_sField;
    }

    /**
     * 设置字段名称
     *
     * @param    field    字段名称
     */
    public void setField(String field) {
        m_sField = field;
    }

    /**
     * 返回字段值
     *
     * @return 字段值
     */
    public Object getValue() {
        return m_oBegin;
    }

    /**
     * 设置字段值
     *
     * @param    value    字段值
     */
    public void setValue(Object value) {
        m_oBegin = value;
    }

    /**
     * 返回范围的起始值
     *
     * @return 范围的起始值
     */
    public Object getBeginValue() {
        return m_oBegin;
    }

    /**
     * 设置范围的起始值
     *
     * @param    value    范围的起始值
     */
    public void setBeginValue(Object value) {
        m_oBegin = value;
    }

    /**
     * 返回范围的结束值
     *
     * @return 范围的结束值
     */
    public Object getCloseValue() {
        return m_oClose;
    }

    /**
     * 设置范围的结束值
     *
     * @param    value    范围的结束值
     */
    public void setCloseValue(Object value) {
        m_oClose = value;
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<Object> iterator() {
        if (m_oOperate != Operate.BETWEEN) {
            return null;
        }

        return new Iterator<Object>() {

            private int m_iIndex = 0;

            /* (non-Javadoc)
             * @see java.util.Iterator#hasNext()
             */
            public boolean hasNext() {
                if (m_iIndex < 2) {
                    return true;
                }
                return false;
            }

            /* (non-Javadoc)
             * @see java.util.Iterator#next()
             */
            public Object next() {
                if (m_iIndex == 0) {
                    m_iIndex++;
                    return m_oBegin;
                }
                if (m_iIndex == 1) {
                    m_iIndex++;
                    return m_oClose;
                }
                return null;
            }

            /* (non-Javadoc)
             * @see java.util.Iterator#remove()
             */
            public void remove() {
                throw new RuntimeException("Not supported method");
            }

        };
    }

    public boolean isCombined() {
        return false;
    }

    public String toQuery() {
        return toQuery(new StringBuffer(32)).toString();
    }

    public StringBuffer toQuery(StringBuffer sb) {
        switch (m_oOperate) {
            case ISNULL:
            case NOTNULL:
                sb.append(m_sField).append(m_oOperate.getOperate());
                break;
            case BETWEEN:
                sb.append(m_sField).append(m_oOperate.getOperate()).append("? and ?");
                break;
            default:
                sb.append(m_sField).append(m_oOperate.getOperate()).append("? ");
        }
        return sb;
    }
}

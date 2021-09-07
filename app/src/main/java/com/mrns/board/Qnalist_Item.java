package com.mrns.board;

public class Qnalist_Item {

    /**
     * Data seq
     */
    private int qnaSeq;

    /**
     * Data count
     */
    private  int ansCount;

    /**
     * Data array
     */
    private String[] qData;

    /**
     * True if this item is selectable
     */
    private boolean qSelectable = true;

    public Qnalist_Item(int seq, int cnt, String[] obj) {

        qnaSeq = seq;
        ansCount = cnt;
        this.qData = new String[obj.length];
        for(int i=0; i<obj.length; i++) {
            this.qData[i] = obj[i];
        }
    }

    public Qnalist_Item(int seq, int cnt, String obj1, String obj2, String obj3) {


        qnaSeq = seq;
        ansCount = cnt;

        qData = new String[3];
        qData[0] = obj1;
        qData[1] = obj2;
        qData[2] = obj3;
    }

    /**
     * True if this item is selectable
     */
    public boolean ispSelectable() { return qSelectable; }

    public int getQnaSeq() { return qnaSeq; }

    public int getAnsCount() { return  ansCount; }

    public String[] getData() {

        String[] ret =null;

        if(this.qData != null){
            ret = new String[this.qData.length];
            for(int i=0; i<this.qData.length; i++){
                ret[i] = this.qData[i];
            }
        }

        return ret;
    }

    public String getData(int index) {
        if(qData == null || index >= qData.length) {
            return null;
        }

        return qData[index];
    }

    public void setqData(String[] obj) {
        this.qData = new String[obj.length];
        for(int i=0; i<obj.length; i++) {
            this.qData[i] = obj[i];
        }

    }

    public void setQnaSeq(int seq) { qnaSeq = seq; }
    public void setAnsCount(int cnt) { ansCount = cnt ;}

    public int compareTo(Qnalist_Item other) {
        if (qData != null) {
            String[] otherData = other.getData();
            if (qData.length == otherData.length) {
                for (int i = 0; i < qData.length; i++) {
                    if (!qData[i].equals(otherData[i])) {
                        return -1;
                    }
                }
            } else {
                return -1;
            }
        } else {
            throw new IllegalArgumentException();
        }

        return 0;
    }

}

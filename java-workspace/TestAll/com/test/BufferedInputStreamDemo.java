package com.test;

import java.io.*;

public class BufferedInputStreamDemo {

    /**
    * @param args
    */
    public static void main(String[] args) throws IOException {
        // TODO �Զ����ɷ������
        String s = "this is &test; a book,that is &test a apple.\n";
        System.out.println(s);
        byte[] b = s.getBytes();
        ByteArrayInputStream bai = new ByteArrayInputStream(b);
        BufferedInputStream bf = new BufferedInputStream(bai);
        int c;
        boolean marked = false;
        while((c=bf.read())!=-1){
            switch(c){
            case '&':
                if(!marked){
                    bf.mark(40);
                    marked = true;
                }else{
                    marked = false;
                }
                break;
            case ';':
                if(marked){
                    marked = false;
                    System.out.print("(c)");
                }else{
                    System.out.print((char)c);
                }
                break;
            case ' ':
                if(marked){
                    marked = false;
                    bf.reset();
                    System.out.print("&");
                }else{
                    System.out.print((char)c);
                }
                break;
            default:
                if(!marked){
                    System.out.print((char)c);
                }
            break;
            }
            try{
                Thread.sleep(100);
            }catch(Exception e){
                System.out.println("E:"+e);
            }
        }
    }

}
//��������
//this is &test; a book,that is &test a apple.
//
//this is (c) a book,that is &test a apple.
//
//��һ����Ҫע�⣬mark()�еĲ������ܴ��ڻ�����Ĭ���ֽڵĴ�С��

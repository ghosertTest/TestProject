package com.test;

import java.io.*;

public class BufferedInputStreamDemo {

    /**
    * @param args
    */
    public static void main(String[] args) throws IOException {
        // TODO 自动生成方法存根
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
//输出结果：
//this is &test; a book,that is &test a apple.
//
//this is (c) a book,that is &test a apple.
//
//有一点需要注意，mark()中的参数不能大于缓冲器默认字节的大小。

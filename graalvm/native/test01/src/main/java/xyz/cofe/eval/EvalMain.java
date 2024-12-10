package xyz.cofe.eval;

public class EvalMain {
    public static void main(String[] args){
        System.out.println("hello");
        for(int i=0;i<args.length;i++){
            System.out.println("arg ["+i+"] = "+args[i]);
        }
    }
}

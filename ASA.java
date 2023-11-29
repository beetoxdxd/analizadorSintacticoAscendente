import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class ASA implements Parser{

    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;
    private List<TipoToken> terminales = new ArrayList<>();


    public ASA(List<Token> tokens){
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
    }

    @Override
    public boolean parse(){
        int reduccion = 0;
        Stack pila=new Stack();

        pila.push(0);

        while(i < tokens.size()){
            if(pila.peek().equals(0)){
                if(reduccion != 0){
                    if(reduccion == 1) pila.push(1);
                    else {
                        System.out.println("Error en la reducción del estado 0");
                        return false;
                    }

                    reduccion = 0;
                } else {
                    if(preanalisis.tipo == TipoToken.SELECT){
                        pila.push(2);
                        next();
                    } else {
                        System.out.println("ERROR ENCONTRADO: Se esperaba 'select'");
                        return false;
                    }
                }
            } else if(pila.peek().equals(1)){
                if(preanalisis.tipo == TipoToken.EOF){
                    System.out.println("Consulta correcta");
                    return true;
                } else {
                    System.out.println("ERROR ENCONTRADO: Se esperaba '$'");
                    return false;
                }
            } else if(pila.peek().equals(2)){
                if(reduccion != 0){
                    if(reduccion == 2) pila.push(3);
                    else if(reduccion == 3) pila.push(5);
                    else if(reduccion == 4) pila.push(7);
                    else if(reduccion == 5) pila.push(8);
                    else {
                        System.out.println("Error en la reducción del estado 2");
                        return false;
                    }

                    reduccion = 0;
                } else {
                    if(preanalisis.tipo == TipoToken.DISTINCT){
                        pila.push(4);
                        next();
                    } else if(preanalisis.tipo == TipoToken.ASTERISCO){
                        pila.push(6);
                        next();
                    } else if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
                        pila.push(9);
                        next();
                    } else {
                        System.out.println("ERROR ENCONTRADO: Se esperaba 'distinct', '*' o 'id'");
                        return false;
                    }
                }
            } else if(pila.peek().equals(3)){
                if(preanalisis.tipo == TipoToken.FROM){
                    pila.push(10);
                    next();
                } else {
                    System.out.println("ERROR ENCONTRADO: Se esperaba 'from'");
                    return false;
                }
            } else if(pila.peek().equals(4)){
                if(reduccion != 0){
                    if(reduccion == 3) pila.push(11);
                    else if(reduccion == 4) pila.push(7);
                    else if(reduccion == 5) pila.push(8);
                    else {
                        System.out.println("Error en la reducción del estado 4");
                        return false;
                    }

                    reduccion = 0;
                } else {
                    if(preanalisis.tipo == TipoToken.ASTERISCO){
                        pila.push(6);
                        next();
                    } else if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
                        pila.push(9);
                        next();
                    } else {
                        System.out.println("ERROR ENCONTRADO: Se esperaba '*' o 'id'");
                        return false;
                    }
                }
            } else if(pila.peek().equals(5)){
                if(preanalisis.tipo == TipoToken.FROM){
                    pila.pop();
                    reduccion = 2;
                } else {
                    System.out.println("ERROR ENCONTRADO: Se esperaba 'from'");
                    return false;
                }
            } else if(pila.peek().equals(6)){
                if(preanalisis.tipo == TipoToken.FROM){
                    pila.pop();
                    reduccion = 3;
                } else {
                    System.out.println("ERROR ENCONTRADO: Se esperaba 'from'");
                    return false;
                }
            } else if(pila.peek().equals(7)){
                if(preanalisis.tipo == TipoToken.FROM){
                    pila.pop();
                    reduccion = 3;
                } else if(preanalisis.tipo == TipoToken.COMA){
                    pila.push(12);
                    next();
                } else {
                    System.out.println("ERROR ENCONTRADO: Se esperaba 'from' o ','");
                    return false;
                }
            } else if(pila.peek().equals(8)){
                if(preanalisis.tipo == TipoToken.FROM || preanalisis.tipo == TipoToken.COMA){
                    pila.pop();
                    reduccion = 4;
                } else {
                    System.out.println("ERROR ENCONTRADO: Se esperaba 'from' o ','");
                    return false;
                }
            } else if(pila.peek().equals(9)){
                if(reduccion != 0){
                    if(reduccion == 6) pila.push(13);
                    else {
                        System.out.println("Error en la reducción del estado 9");
                        return false;
                    }

                    reduccion = 0;
                } else {
                    if(preanalisis.tipo == TipoToken.FROM || preanalisis.tipo == TipoToken.COMA){
                        reduccion = 6;
                    } else if(preanalisis.tipo == TipoToken.PUNTO){
                        pila.push(14);
                        next();
                    } else {
                        System.out.println("ERROR ENCONTRADO: Se esperaba 'from', ',' o '.'");
                        return false;
                    }
                }
            } else if(pila.peek().equals(10)){
                if(reduccion != 0){
                    if(reduccion == 7) pila.push(15);
                    else if(reduccion == 8) pila.push(16);
                    else {
                        System.out.println("Error en la reducción del estado 10");
                        return false;
                    }

                    reduccion = 0;
                } else {
                    if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
                        pila.push(17);
                        next();
                    } else {
                        System.out.println("ERROR ENCONTRADO: Se esperaba 'id'");
                        return false;
                    }
                }
            } else if(pila.peek().equals(11)){
                if(preanalisis.tipo == TipoToken.FROM){
                    pila.pop(); pila.pop();
                    reduccion = 2;
                } else {
                    System.out.println("ERROR ENCONTRADO: Se esperaba 'from'");
                    return false;
                }
            } else if(pila.peek().equals(12)){
                if(reduccion != 0){
                    if(reduccion == 5) pila.push(18);
                    else {
                        System.out.println("Error en la reducción del estado 12");
                        return false;
                    }

                    reduccion = 0;
                } else {
                    if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
                        pila.push(9);
                        next();
                    } else {
                        System.out.println("ERROR ENCONTRADO: Se esperaba 'id'");
                        return false;
                    }
                }
            } else if(pila.peek().equals(13)){
                if(preanalisis.tipo == TipoToken.FROM || preanalisis.tipo == TipoToken.COMA){
                    pila.pop(); pila.pop();
                    reduccion = 5;
                } else {
                    System.out.println("ERROR ENCONTRADO: Se esperaba 'from' o ','");
                    return false;
                }
            } else if(pila.peek().equals(14)){
                if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
                    pila.push(19);
                    next();
                } else {
                    System.out.println("ERROR ENCONTRADO: Se esperaba 'id'");
                    return false;
                }
            } else if(pila.peek().equals(15)){
                if(preanalisis.tipo == TipoToken.COMA){
                    pila.push(20);
                    next();
                } else if(preanalisis.tipo == TipoToken.EOF){
                    pila.pop(); pila.pop(); pila.pop(); pila.pop();
                    reduccion = 1;
                } else {
                    System.out.println("ERROR ENCONTRADO: Se esperaba ',' o '$'");
                    return false;
                }
            } else if(pila.peek().equals(16)){
                if(preanalisis.tipo == TipoToken.COMA || preanalisis.tipo == TipoToken.EOF){
                    pila.pop();
                    reduccion = 7;
                } else {
                    System.out.println("ERROR ENCONTRADO: Se esperaba ',' o '$'");
                    return false;
                }
            } else if(pila.peek().equals(17)){
                if(reduccion != 0){
                    if(reduccion == 9) pila.push(21);
                    else {
                        System.out.println("Error en la reducción del estado 17");
                        return false;
                    }

                    reduccion = 0;
                } else {
                    if(preanalisis.tipo == TipoToken.COMA || preanalisis.tipo == TipoToken.EOF){
                        reduccion = 9;
                    } else if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
                        pila.push(22);
                        next();
                    } else {
                        System.out.println("ERROR ENCONTRADO: Se esperaba ',', 'id' o '$'");
                        return false;
                    }
                }
            } else if(pila.peek().equals(18)){
                if(preanalisis.tipo == TipoToken.FROM || preanalisis.tipo == TipoToken.COMA){
                    pila.pop(); pila.pop(); pila.pop();
                    reduccion = 4;
                } else {
                    System.out.println("ERROR ENCONTRADO: Se esperaba 'from' o ','");
                    return false;
                }
            } else if(pila.peek().equals(19)){
                if(preanalisis.tipo == TipoToken.FROM || preanalisis.tipo == TipoToken.COMA){
                    pila.pop(); pila.pop();
                    reduccion = 6;
                } else {
                    System.out.println("ERROR ENCONTRADO: Se esperaba 'from' o ','");
                    return false;
                }
            } else if(pila.peek().equals(20)){
                if(reduccion != 0){
                    if(reduccion == 8) pila.push(23);
                    else {
                        System.out.println("Error en la reducción del estado 20");
                        return false;
                    }

                    reduccion = 0;
                } else {
                    if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
                        pila.push(17);
                        next();
                    } else {
                        System.out.println("ERROR ENCONTRADO: Se esperaba 'id'");
                        return false;
                    }
                }
            } else if(pila.peek().equals(21)){
                if(preanalisis.tipo == TipoToken.COMA || preanalisis.tipo == TipoToken.EOF){
                    pila.pop(); pila.pop();
                    reduccion = 8;
                } else {
                    System.out.println("ERROR ENCONTRADO: Se esperaba ',' o '$'");
                    return false;
                }
            } else if(pila.peek().equals(22)){
                if(preanalisis.tipo == TipoToken.COMA || preanalisis.tipo == TipoToken.EOF){
                    pila.pop();
                    reduccion = 9;
                } else {
                    System.out.println("ERROR ENCONTRADO: Se esperaba ',' o '$'");
                    return false;
                }
            } else if(pila.peek().equals(23)){
                if(preanalisis.tipo == TipoToken.COMA || preanalisis.tipo == TipoToken.EOF){
                    pila.pop(); pila.pop(); pila.pop();
                    reduccion = 7;
                } else {
                    System.out.println("ERROR ENCONTRADO: Se esperaba ',' o '$'");
                    return false;
                }
            }
        }

        return false;
    }

    private void next(){
        i++;
        preanalisis = tokens.get(i);
    }
}

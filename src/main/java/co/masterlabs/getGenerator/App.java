package co.masterlabs.getGenerator;
import java.io.File;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;


/*
    Program służący do generowania setterów i getterów poza środowiskiem Domino
    dane wejściowe do programu:
        ścieżka do pliku .java / .txt, w którym znajduje się lista pól w formacie:
        {
            int pole1;
            List<Object> lista_czegoś : ileTegoCzegoś;
            itd
        }}

    dane wyjściowe:
        wygenerowany w dokumentach użytkownika plik .txt z getterami i setterami; dla list generuje gettery w postaci getLista_czegoś1, getLista_czegoś2 itd
        przydatne jak nie da się wrzucić rzeczy w metadata przy używaniu xdocReport
            patrz: Donoria, moduł BUF, AgentAnaliza i tam po 25 getterów do listy w klasach, których nie wygeneruje upośledzony DominoDesigner;
                   w dokumentach szablonowych w konfiguracji pola pt sztuki1, sztuki2 etc;

*/



public class App 
{
    public static void main( String[] args )
    { 
        try{

            List<String> list = JavaClassParser.parseClassFile(new File(args[0]));
        }
        catch (InvalidAttributesException e){
            e.printStackTrace();
        }
    }
}

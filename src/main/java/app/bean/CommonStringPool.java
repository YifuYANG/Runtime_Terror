package app.bean;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CommonStringPool implements CommandLineRunner {
    private List<String> list = new ArrayList<String>();

    public boolean ifContainCommonString(String password){
        for(String string : list){
            if(password.contains(string)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void run(String... args) throws Exception {
        File file = new File("src/main/resources/10k-most-common.txt");
        Scanner input = new Scanner(file);

        while (input.hasNextLine()) {
            list.add(input.nextLine());
        }
    }
}

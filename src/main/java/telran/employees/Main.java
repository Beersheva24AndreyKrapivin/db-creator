package telran.employees;

import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;
import telran.view.StandartInputOutput;

public class Main {
    public static void main(String[] args) {
        // TODO generating employees-sql-data.csv (see README)
        // example: :
        // 1,Employee, 15000,QA,,,,,
        // 2,Manager,12000,QA,2.0,,,,
        // 3,WageEmployee,12000,QA,,100,100,,
        // 4,SalesPerson,12000,QA,,100,100,0.1,100000
        InputOutput io = new StandartInputOutput();
        Company company = new CompanyImpl();
        Item[] items = CompanyItems.getItems(company);
        Menu menu = new Menu("Generating CSV file", items);
        menu.perform(io);
        io.writeLine("Application is finished");
    }
}
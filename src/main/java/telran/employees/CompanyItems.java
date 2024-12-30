package telran.employees;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

import org.json.JSONObject;

import telran.view.*;

public class CompanyItems {
    final static long MIN_ID = 1000;
    final static long MAX_ID = 10_000;
    final static int MIN_SALARY = 5000;
    final static int MAX_SALARY = 50000;
    final static String[] DEPARTMENTS = { "QA", "Development", "Devops", "Sales" };
    final static int MIN_WAGE = 30;
    final static int MAX_WAGE = 300;
    final static int MIN_HOURS = 1;
    final static int MAX_HOURS = 200;
    final static float MIN_PERCENT = 0.1f;
    final static float MAX_PERCENT = 1.5f;
    final static long MIN_SALES = 1;
    final static long MAX_SALES = Long.MAX_VALUE;
    final static float MIN_FACTOR = 1.5f;
    final static float MAX_FACTOR = 4f;
    final static int MIN_COUNT = 0;
    final static int MAX_COUNT = 100;
    final static String FILE_NAME = "employees-sql-data.csv";
    static final Random RANDOM = new Random();

    private static Company company;

    public static Item[] getItems(Company company) {
        CompanyItems.company = company;
        Item[] res = {
                Item.of("Generate employees", CompanyItems::generateEmployees),
                Item.ofExit()
        };
        return res;
    }

    static void generateEmployees(InputOutput io) {
        String department = enterDepartment(io);
        int countManagers = enterEmpl(io, "Managers");
        int countEmployees = enterEmpl(io, "Employees");
        int countWageEmployees = enterEmpl(io, "Wage Employees");
        int countSalesPersons = enterEmpl(io, "Sales Persons");

        for (int i = 0; i < countManagers; i++) {
            addManager(department);
        }

        for (int i = 0; i < countEmployees; i++) {
            addEmployees(department);
        }

        for (int i = 0; i < countWageEmployees; i++) {
            addWageEmployees(department);
        }

        for (int i = 0; i < countSalesPersons; i++) {
            addSalesPersons(department);
        }
    }

    private static void addSalesPersons(String department) {
        long id = RANDOM.nextLong(MIN_ID, MAX_ID);
        int salary = RANDOM.nextInt(MIN_SALARY, MAX_SALARY);
        int wage = RANDOM.nextInt(MIN_WAGE, MAX_WAGE);
        int hours = RANDOM.nextInt(MIN_HOURS, MAX_HOURS);
        float percent = RANDOM.nextFloat(MIN_PERCENT, MAX_PERCENT);
        long sales = RANDOM.nextLong(MIN_SALES, MAX_SALES);
        saveToFile(new SalesPerson(id, salary, department, wage, hours, percent, sales), salary);
    }

    private static void addWageEmployees(String department) {
        long id = RANDOM.nextLong(MIN_ID, MAX_ID);
        int salary = RANDOM.nextInt(MIN_SALARY, MAX_SALARY);
        int wage = RANDOM.nextInt(MIN_WAGE, MAX_WAGE);
        int hours = RANDOM.nextInt(MIN_HOURS, MAX_HOURS);
        saveToFile(new WageEmployee(id, salary, department, wage, hours), salary);
    }

    private static void addEmployees(String department) {
        long id = RANDOM.nextLong(MIN_ID, MAX_ID);
        int salary = RANDOM.nextInt(MIN_SALARY, MAX_SALARY);
        saveToFile(new Employee(id, salary, department), salary);
    }

    private static void addManager(String department) {
        long id = RANDOM.nextLong(MIN_ID, MAX_ID);
        int salary = RANDOM.nextInt(MIN_SALARY, MAX_SALARY);
        float factor = RANDOM.nextFloat(MIN_FACTOR, MAX_FACTOR);
        saveToFile(new Manager(id, salary, department, factor), salary);
    }

    static void saveToFile(Employee empl, int basicSalary) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            String str = "";
            if (empl instanceof SalesPerson salesPerson) {
                str = getSalesPerson(salesPerson, basicSalary);
            } else if (empl instanceof WageEmployee wageEmployee) {
                str = getWageEmployee(wageEmployee, basicSalary);
            } else if (empl instanceof Manager manager) {
                str = getManager(manager, basicSalary);
            } else if (empl instanceof Employee) {
                str = getEmployee(empl, basicSalary);
            }
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String getSalesPerson(SalesPerson salesPerson, int basicSalary) {
        return "" + salesPerson.getId() + ",SalesPerson," + basicSalary + "," + salesPerson.getDepartment() + ",," + salesPerson.getWage() + "," + salesPerson.getHours() + "," + salesPerson.getSales() + "\n";
    }

    private static String getWageEmployee(WageEmployee wageEmployee, int basicSalary) {
        return "" + wageEmployee.getId() + ",WageEmployee," + basicSalary + "," + wageEmployee.getDepartment() + ",," + wageEmployee.getWage() + "," + wageEmployee.getHours() + ",,\n";
    }

    private static String getManager(Manager manager, int basicSalary) {
        return "" + manager.getId() + ",Manager," + basicSalary + "," + manager.getDepartment() + "," + manager.getFactor() + ",,,,\n";
    }

    private static String getEmployee(Employee empl, int basicSalary) {
        return "" + empl.getId() + ",Employee," + basicSalary + "," + empl.getDepartment() + ",,,,,\n";
    }

    private static String enterDepartment(InputOutput io) {
        HashSet<String> departmentsSet = new HashSet<>(List.of(DEPARTMENTS));
        return io.readStringOptions("Enter department from " + departmentsSet, "Must be one out from " + departmentsSet,
                departmentsSet);
    }

    private static int enterEmpl(InputOutput io, String empl) {
        return io.readNumberRange(
                String.format("Enter count of %s value in the range [%d-%d]", empl, MIN_COUNT, MAX_COUNT),
                "Wrong Salary value", MIN_COUNT, MAX_COUNT).intValue();
    }

}

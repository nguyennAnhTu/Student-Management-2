import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        StudentManagement studentmanagement = new StudentManagement();
        Scanner sc = new Scanner(System.in);
        String Id;

        while (true) {
            System.out.println("1. Add Student");
            System.out.println("2. Display All Students");
            System.out.println("3. Display a student by ID");
            System.out.println("4. Delete a student by ID");
            System.out.println("5. Update a student by ID");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 0:
                    System.out.println("Goodbye!");
                    System.exit(0);
                case 1:
                    Student newstudent = new Student();
                    String id;
                    String name;
                    int age;
                    String gender;
                    while (true) {
                        System.out.print("Enter student id: ");
                        id = sc.nextLine();
                        if (!studentmanagement.existedId(id)) break;
                        else {
                            System.out.println("Id already exists!");
                        }
                    }
                    System.out.print("Enter student name: ");
                    name = sc.nextLine();
                    System.out.print("Enter student age: ");
                    age = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter student gender: ");
                    gender = sc.next();
                    newstudent.setId(id);
                    newstudent.setName(name);
                    newstudent.setAge(age);
                    newstudent.setGender(gender);
                    studentmanagement.add(newstudent);
                    break;
                case 2:
                    studentmanagement.displayListStudent();
                    break;
                case 3:
                    System.out.print("Enter student id: ");
                    Id = sc.nextLine();
                    studentmanagement.display(Id);
                    break;
                case 4:
                    System.out.print("Enter student id: ");
                    Id = sc.nextLine();
                    studentmanagement.delete(Id);
                    break;
                case 5:
                    System.out.print("Enter student id: ");
                    Id = sc.next();
                    studentmanagement.update(Id);
                    break;
                default:
                    System.out.println("Invalid choice! Only choice 0-5");
                    break;
            }
        }
    }
}

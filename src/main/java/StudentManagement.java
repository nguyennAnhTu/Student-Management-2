import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

class StudentManagement implements Management {
    private static final String existQuery = "SELECT 1 FROM student WHERE id = ?";
    private static final String insertQuery = "INSERT INTO student (id, name, age, gender) VALUES (?, ?, ?, ?)";
    private static final String selectQuery = "SELECT * FROM student WHERE id = ?";
    private static final String deleteQuery = "DELETE FROM student WHERE id = ?";
    private static final String selectAllQuery = "SELECT * FROM student";
    private static final String updateQuery = "UPDATE student SET name = ?, age = ?, gender = ? WHERE id = ?";
    private static final DatabaseConfig databaseConfig = DatabaseConfig.getInstance();
    public static boolean existedId(String id) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            //connection = DatabaseConfig.getInstance().connect();
            connection = databaseConfig.connect();
            if (connection != null) {
                ps = connection.prepareStatement(existQuery);
                ps.setString(1, id);
                rs = ps.executeQuery();
                exists = rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error checking ID existence: " + e.getMessage());
        } finally {
            DatabaseConfig.closeResources(connection, ps, rs);
        }
        return exists;
    }

    public void add(Student student) {
        if (!existedId(student.getId())) {
            Connection connection = null;
            PreparedStatement ps = null;

            try {
                connection = databaseConfig.connect();
                if (connection != null) {
                    databaseConfig.beginTransaction(connection);
                    ps = connection.prepareStatement(insertQuery);
                    ps.setString(1, student.getId());
                    ps.setString(2, student.getName());
                    ps.setInt(3, student.getAge());
                    ps.setString(4, student.getGender());
                    ps.executeUpdate();

                    databaseConfig.commitTransaction(connection);
                    System.out.println("Student added successfully.");
                }
            } catch (SQLException e) {
                System.out.println("Error adding student: " + e.getMessage());
            } finally {
                DatabaseConfig.closeResources(connection, ps, null);
            }
        }
        else {
            System.out.println("Student already exists");
        }
    }

    public String toString(ResultSet rs) {
        try {
            String studentId = rs.getString("id");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            String gender = rs.getString("gender");
            return studentId + "\t" + name + "\t" + age + "\t" + gender;
        } catch (SQLException e) {
            System.out.println("Error converting ResultSet to string: " + e.getMessage());
        }
        return "";
    }

    @Override
    public void displayById(String id) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = databaseConfig.connect();
            if (connection != null) {
                ps = connection.prepareStatement(selectQuery);
                ps.setString(1, id);
                rs = ps.executeQuery();
                if (rs.next()) {
                    System.out.println(toString(rs));
                } else {
                    System.out.println("Student does not exist");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error displaying student: " + e.getMessage());
        } finally {
            DatabaseConfig.closeResources(connection, ps, rs);
        }
    }

    public void displayListStudent() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = databaseConfig.connect();
            if (connection != null) {
                ps = connection.prepareStatement(selectAllQuery);
                rs = ps.executeQuery();
                while (rs.next()) {
                    System.out.println(toString(rs));
                }
            }
        } catch (SQLException e) {
            databaseConfig.rollbackTransaction(connection);
            System.out.println("Error displaying students list: " + e.getMessage());
        } finally {
            DatabaseConfig.closeResources(connection, ps, rs);
        }
    }

    @Override
    public void update(String id) {
        if (existedId(id)) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter new name: ");
            String name = sc.nextLine();
            System.out.print("Enter new age: ");
            int age = sc.nextInt();
            sc.nextLine();  // consume newline
            System.out.print("Enter new gender: ");
            String gender = sc.nextLine();

            Connection connection = null;
            PreparedStatement ps = null;

            try {
                connection = databaseConfig.connect();
                if (connection != null) {
                    databaseConfig.beginTransaction(connection);
                    ps = connection.prepareStatement(updateQuery);
                    ps.setString(1, name);
                    ps.setInt(2, age);
                    ps.setString(3, gender);
                    ps.setString(4, id);
                    ps.executeUpdate();

                    databaseConfig.commitTransaction(connection);
                    System.out.println("Student with Id: " + id + " updated");
                }
            } catch (SQLException e) {
                databaseConfig.rollbackTransaction(connection);
                System.out.println("Error updating student: " + e.getMessage());
            } finally {
                DatabaseConfig.closeResources(connection, ps, null);
            }
        } else {
            System.out.println("Student does not exist");
        }
    }

    @Override
    public void delete(String id) {
        if (existedId(id)) {
            Connection connection = null;
            PreparedStatement ps = null;

            try {
                connection = databaseConfig.connect();
                if (connection != null) {
                    databaseConfig.beginTransaction(connection);
                    ps = connection.prepareStatement(deleteQuery);
                    ps.setString(1, id);
                    ps.executeUpdate();

                    databaseConfig.commitTransaction(connection);
                    System.out.println("Student with Id: " + id + " deleted");
                }
            } catch (SQLException e) {
                databaseConfig.rollbackTransaction(connection);
                System.out.println("Error deleting student: " + e.getMessage());
            } finally {
                DatabaseConfig.closeResources(connection, ps, null);
            }
        } else {
            System.out.println("Student does not exist");
        }
    }
}
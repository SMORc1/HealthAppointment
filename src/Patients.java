public class Patients {
    private String username;
    private String password;
    private String name;
    private int age;
    private String contact;

    public Patients(String username, String password, String name, int age, String contact) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.age = age;
        this.contact = contact;
    }

    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getContact() { return contact; }

    @Override
    public String toString() {
        return name + " (Age: " + age + ", Contact: " + contact + ")";
    }
}

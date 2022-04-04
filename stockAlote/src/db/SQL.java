package db;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class SQL {
    private Connection conn;
    private Statement st;
    private ResultSet rs = null;

    public SQL() {
        String url = "jdbc:postgresql://db.fe.up.pt/meec1a0303";
        String user = "meec1a0303";
        String password = "stockAlote20";
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
            st = conn.createStatement();
            st.execute("SET search_path TO dba03g03");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection() {return conn;}
    public Statement getStatement() {return st;}
    public ResultSet getResultSet(){ return rs;}
    public String getStringFromRS(String parameter){
        String s = null;
        try {
            s = rs.getString(parameter);
        }catch (Exception e) {
            System.out.println("Sql exception: RS error");
        }
        return s;
    }
    public int getIntFromRS(String parameter){
        int i = 0;
        try {
            i = rs.getInt(parameter);
        }catch (Exception e) {
            System.out.println("Sql exception: RS error");
        }
        return i;
    }
    public boolean getBoolFromRS(String parameter){
        boolean b = false;
        try {
            b = rs.getBoolean(parameter);
        }catch (Exception e) {
            System.out.println("Sql exception: RS error");
        }
        return b;
    }


    public boolean hasRows(String table){
        try {
            rs = st.executeQuery("SELECT * FROM "+table);
        } catch (Exception ex) {
            rs = null;
            return false;
        }

        try{
            if(rs.next()==false) return false;
            else return true;
        }catch(Exception ex){
            System.out.println("No rows");
        }
        return false;
    }
    public List<Object> getUserFields(String role){
        String name = getStringFromRS("name");
        String birthdate = getStringFromRS("birthdate");
        String contact = getStringFromRS("contact");
        String username = getStringFromRS("username");
        String pw = getStringFromRS("password");

        return Arrays.asList(name, contact, birthdate, username, pw,role);
    }
    public List<Object> getWHFields(){
        try {
            rs = st.executeQuery("SELECT * FROM warehouse");
            rs.next();
        }catch(Exception e){
            System.out.println("SQL exception: warehouse fields error");
        }

        String name = getStringFromRS("name_wh");
        String location = getStringFromRS("location");
        int capacity = getIntFromRS("m3");

        return Arrays.asList(name,location,capacity);
    }
    public List<Object> getSectionFields(){
        String category = getStringFromRS("category_section");
        int m3 = getIntFromRS("m3");

        return Arrays.asList(category, m3);
    }
    public List<Object> getGoodsFields(){
        String name = getStringFromRS("name_goods");
        String cat = getStringFromRS("category_section");
        int qty = getIntFromRS("qty");
        int weight_pu = getIntFromRS("weight_pu");
        int volume_pu = getIntFromRS("volume_pu");
        boolean fragile = getBoolFromRS("fragility");

        return Arrays.asList(name,cat,qty,weight_pu,volume_pu,fragile);
    }
    public List<Object> getTaskFields(){

        int id_task = getIntFromRS("id_task");
        String type = getStringFromRS("type_task");
        String name = getStringFromRS("product_name");
        String cat = getStringFromRS("product_category");
        String dest = getStringFromRS("destination_category");
        int qty = getIntFromRS("qty");
        boolean is_completed = getBoolFromRS("is_completed");
        String start_time = getStringFromRS("start_time");
        String end_time = getStringFromRS("end_time");
        String category_section = getStringFromRS("category_section");
        String done_by = getStringFromRS("done_by");

        return Arrays.asList(id_task, type, name, cat, dest, qty, is_completed, start_time, end_time, category_section,done_by);
    }

    public void executeQuery(String str){
        try {
            st.executeUpdate(str);
        } catch (Exception e) {
            System.out.println("Sql exception: query error");
        }
    }

    public void insertUser(String name, String birthdate, String contact, String username, String password, String name_wh, String role) {
        String query = "INSERT INTO "+role+" VALUES('"+name+"','"+birthdate+"','"+contact+"','"+username+"','"+password+"','"+name_wh+"');";
        executeQuery(query);
    }
    public void updateUsername( String newUsername, String oldUsername, String role) {

        String query= "UPDATE " +role+" SET username='"+newUsername+"' WHERE username='"+oldUsername+"' ";
        executeQuery(query);
    }

    public void updatePassword( String username, String newPassword,  String role){
        String query= "UPDATE " +role+" SET password='"+newPassword+"' WHERE username='"+username+"' ";
        executeQuery(query);
    }

    public void deleteUser(String username) {
        String query;
        query = "DELETE FROM users WHERE username='"+username+"'";
        executeQuery(query);
    }
    public void insertWH(String name, String location, int m3){
        String query = "INSERT INTO warehouse VALUES('"+name+"','"+location+"',"+m3+");";
        executeQuery(query);
    }

    public void insertSection(String category, int m3, String WHname){
        String query = "INSERT INTO section VALUES('"+category+"',"+m3+",'"+WHname+"');";
        executeQuery(query);
    }

    public void deleteSection(String category){
        //falta apagar goods e tasks da section
        String query;
        query = "DELETE FROM task WHERE category_section='"+category+"'";
        executeQuery(query);
        query = "DELETE FROM goods WHERE category_section='"+category+"'";
        executeQuery(query);
        query = "DELETE FROM section WHERE category_section='"+category+"'";
        executeQuery(query);
    }

    public void insertTask(int id, String type, String pname, String pcategory, String dest, int qty,
                           boolean is_completed, String start_time, String end_time, String doneBy){
        String query = "INSERT INTO task VALUES("+id+",'"+ type+"','"+ pname+"','"+ pcategory+"','" + dest + "'," + qty +","
                + is_completed+",'"+ start_time+"','"+end_time+"','"+doneBy+"','"+pcategory+"');";
        executeQuery(query);
    }

    public void removeTask(int id, String category){
        String query;
        query = "DELETE FROM task WHERE category_section='"+category+"' AND id_task="+id;
        executeQuery(query);
    }

    public void updateTaskStart(int id, String category, String start_time, String doneby){
        String query = "UPDATE task SET start_time='"+start_time+ "' WHERE category_section='"+category+"' AND id_task="+id;
        executeQuery(query);
        query = "UPDATE task SET done_by='"+doneby+ "' WHERE category_section='"+category+"' AND id_task="+id;
        executeQuery(query);
    }

    public void updateTaskEnd(int id, String category, String end_time){
        String query = "UPDATE task SET end_time='"+end_time+"' WHERE category_section='"+category+"' AND id_task="+id;
        executeQuery(query);
        query = "UPDATE task SET is_completed="+true+" WHERE category_section='"+category+"' AND id_task="+id;
        executeQuery(query);
    }

    public void updateTaskId(int id, String category){
        String query = "UPDATE task SET id_task="+(id-1)+" WHERE category_section='"+category+"' AND id_task="+id;
        executeQuery(query);
    }

    public void insertGood(int id, String name, int qty, int weight_pu, int volume_pu, boolean fragility, String category ){
        String query = "INSERT INTO goods VALUES("+id+",'"+ name+"',"+qty+","+weight_pu+","+volume_pu+","+fragility+",'"+category+"');";
        executeQuery(query);
    }

    public void updateGoodQTY(String name, String category, int newqty){
        String query = "UPDATE goods SET qty="+newqty+" WHERE category_section='"+category+"' AND name_goods='"+name+"'";
        executeQuery(query);
    }

    public void createSQLtables() {
        try {
            st.execute("CREATE TABLE IF NOT EXISTS warehouse(name_wh VARCHAR(20) NOT NULL,location VARCHAR(20) NOT NULL,m3 INT NOT NULL,CONSTRAINT PK_warehouse PRIMARY KEY (name_wh));");
            st.execute("CREATE TABLE IF NOT EXISTS section(category_section VARCHAR(20) NOT NULL,m3 INT NOT NULL,name_wh VARCHAR(20) NOT NULL,CONSTRAINT PK_section PRIMARY KEY (category_section));");
            st.execute("CREATE TABLE IF NOT EXISTS goods(id_goods INT NOT NULL,name_goods VARCHAR(20) NOT NULL,qty INT NOT NULL,weight_pu INT NOT NULL,volume_pu INT NOT NULL,fragility  BOOLEAN,category_section VARCHAR(20) NOT NULL,CONSTRAINT PK_goods PRIMARY KEY (id_goods, category_section));");
            st.execute("CREATE TABLE IF NOT EXISTS task(id_task INT NOT NULL,type_task VARCHAR(20) NOT NULL,product_name VARCHAR(20) NOT NULL,product_category VARCHAR(20) NOT NULL, destination_category VARCHAR(20) NOT NULL,qty INT NOT NULL,is_completed  BOOLEAN,start_time VARCHAR(50) NOT NULL,end_time VARCHAR(50) NOT NULL,done_by VARCHAR(50),category_section VARCHAR(20) NOT NULL,CONSTRAINT PK_task PRIMARY KEY (id_task, category_section));");
            st.execute("CREATE TABLE IF NOT EXISTS users(name VARCHAR(50),birthdate VARCHAR(10),contact VARCHAR(20),username VARCHAR(20),password VARCHAR(200),name_wh VARCHAR(20) NOT NULL,CONSTRAINT PK_users PRIMARY KEY (username));");
            st.execute("CREATE TABLE IF NOT EXISTS boss()INHERITS (users);");
            st.execute("CREATE TABLE IF NOT EXISTS employee()INHERITS (users);");
            st.execute("CREATE TABLE IF NOT EXISTS manager()INHERITS (users);");
            st.execute("ALTER TABLE section ADD CONSTRAINT FK_section_name_wh FOREIGN KEY (name_wh) REFERENCES warehouse (name_wh) ON DELETE NO ACTION ON UPDATE NO ACTION;");
            st.execute("ALTER TABLE goods ADD CONSTRAINT FK_goods_category_section FOREIGN KEY (category_section) REFERENCES section (category_section) ON DELETE NO ACTION ON UPDATE NO ACTION;");
            st.execute("ALTER TABLE task ADD CONSTRAINT FK_task_category_section FOREIGN KEY (category_section) REFERENCES section (category_section) ON DELETE NO ACTION ON UPDATE NO ACTION;");
            st.execute("ALTER TABLE users ADD CONSTRAINT FK_users_name_wh FOREIGN KEY (name_wh) REFERENCES warehouse (name_wh) ON DELETE NO ACTION ON UPDATE NO ACTION;");
            st.execute("ALTER TABLE boss ADD CONSTRAINT FK_boss_name_wh FOREIGN KEY (name_wh) REFERENCES warehouse (name_wh) ON DELETE NO ACTION ON UPDATE NO ACTION;");
            st.execute("ALTER TABLE employee ADD CONSTRAINT FK_employee_name_wh FOREIGN KEY (name_wh) REFERENCES warehouse (name_wh) ON DELETE NO ACTION ON UPDATE NO ACTION;");
            st.execute("ALTER TABLE manager ADD CONSTRAINT FK_manager_name_wh FOREIGN KEY (name_wh) REFERENCES warehouse (name_wh) ON DELETE NO ACTION ON UPDATE NO ACTION;");
        } catch (Exception e) {
        System.out.println("Sql exception: Creating tables error");
        }
    }

    public void dropSQLtables(){
        try {
            st.execute("DROP TABLE IF EXISTS warehouse CASCADE");
            st.execute("DROP TABLE IF EXISTS section CASCADE");
            st.execute("DROP TABLE IF EXISTS goods CASCADE");
            st.execute("DROP TABLE IF EXISTS task CASCADE");
            st.execute("DROP TABLE IF EXISTS users CASCADE");
            st.execute("DROP TABLE IF EXISTS boss CASCADE");
            st.execute("DROP TABLE IF EXISTS employee CASCADE");
            st.execute("DROP TABLE IF EXISTS manager CASCADE");
        }catch (Exception e) {
        System.out.println("Sql exception: Dropping tables error");
        }
    }

}

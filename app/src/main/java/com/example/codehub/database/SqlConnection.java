package com.example.codehub.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;

import com.example.codehub.models.*;
import com.example.codehub.security.MD5;
import com.example.codehub.utils.SQLDate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlConnection {
    private final Context context;

    public SqlConnection(Context context) {
        this.context = context;
    }

    @SuppressLint("NewApi")
    private Connection connect() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String URL = "jdbc:jtds:sqlserver://10.0.2.2:1433;databaseName=CodeHub";
            return DriverManager.getConnection(URL, "sa", "1211");
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private int getUserCurrent() {
        SharedPreferences preferences = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        return preferences.getInt("id", 0);
    }

    private void saveUserCurrent(int id) {
        SharedPreferences preferences = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("id", id);
        editor.apply();
    }

    public boolean login(String username, String password) {
        try (Connection connection = connect()) {
            if (connection != null) {
                String query = "SELECT * FROM Users WHERE (Username=? OR Email=?) AND PasswordHash=? " +
                        "AND IsActive = 1";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, username);//username
                    statement.setString(2, username);//email
                    statement.setString(3, MD5.hash(password));
                    try (ResultSet rs = statement.executeQuery()) {
                        if (rs.next()) {
                            saveUserCurrent(rs.getInt("ID"));
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            } else return false;
        } catch (SQLException ex) {
            Log.e("SQLException", "login: ", ex);
            return false;
        } catch (Exception e) {
            Log.e("Exception", "login: ", e);
            throw new RuntimeException(e);
        }
    }

    public boolean register(User user) {
        try (Connection connection = connect()) {
            if (connection != null) {
                String query = "INSERT INTO Users (Username, PasswordHash, FirstName, LastName, Email, Gender, DateOfBirth, Currency, IsActive) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, user.getUsername());
                    statement.setString(2, MD5.hash(user.getPassword()));
                    statement.setString(3, user.getFirstName());
                    statement.setString(4, user.getLastName());
                    statement.setString(5, user.getEmail());
                    statement.setBoolean(6, user.isGender());
                    statement.setDate(7, SQLDate.convert(user.getDob()));
                    statement.setInt(8, 0);
                    statement.setBoolean(9, true);
                    int status = statement.executeUpdate();
                    return status > 0;
                }
            } else return false;
        } catch (SQLException ex) {
            Log.e("SQLException", "register: ", ex);
            return false;
        } catch (Exception e) {
            Log.e("Exception", "register: ", e);
            throw new RuntimeException(e);
        }
    }

    public boolean checkAccount() {
        int id = getUserCurrent();
        if (id > 0) {
            try (Connection connection = connect()) {
                if (connection != null) {
                    String query = "SELECT IsActive FROM Users WHERE ID = ?";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setInt(1, id);
                        try (ResultSet rs = statement.executeQuery()) {
                            if (rs.next()) {
                                return rs.getBoolean("IsActive");
                            } else {
                                return false;
                            }
                        }
                    }
                } else return false;
            } catch (SQLException ex) {
                Log.e("SQLException", "checkAccount: ", ex);
                return false;
            }
        } else return false;
    }

    public List<Source> listSource() {
        try (Connection connection = connect()) {
            if (connection != null) {
                List<Source> list = new ArrayList<>();
                String query = "SELECT sc.ID, sc.Name, sc.Fee, iu.Url " +
                        "FROM SourceCodes sc " +
                        "OUTER APPLY (SELECT TOP 1 Url FROM ImageUrls iu WHERE iu.Source = sc.ID) iu " +
                        "WHERE sc.IsShow != 0 AND sc.IsDelete != 1";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    try (ResultSet rs = statement.executeQuery()) {
                        while (rs.next()) {
                            list.add(new Source(
                                    rs.getInt("ID"),
                                    rs.getString("Name"),
                                    rs.getInt("Fee"),
                                    rs.getString("Url")
                            ));
                        }
                        return list;
                    }
                }
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Log.e("SQLException", "listSource: ", ex);
            return null;
        }
    }

    public List<Source> listSource(String search, String language) {
        try (Connection connection = connect()) {
            if (connection != null) {
                List<Source> list = new ArrayList<>();
                String query = "SELECT sc.ID, sc.Name, sc.Fee, l.Name as Language, iu.Url " +
                        "FROM SourceCodes sc " +
                        "INNER JOIN Languages l ON sc.LanguageID = l.ID " +
                        "OUTER APPLY (SELECT TOP 1 Url FROM ImageUrls iu WHERE iu.Source = sc.ID) iu " +
                        "WHERE sc.Name LIKE ? AND sc.IsShow != 0 AND sc.IsDelete != 1";
                if (!language.equals("Tất cả")) {
                    query += " AND l.Name = ?";
                }

                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, "%" + search + "%");
                    if (!language.equals("Tất cả")) {
                        statement.setString(2, language);
                    }
                    try (ResultSet rs = statement.executeQuery()) {
                        while (rs.next()) {
                            list.add(new Source(
                                    rs.getInt("ID"),
                                    rs.getString("Name"),
                                    rs.getInt("Fee"),
                                    rs.getString("Language"),
                                    rs.getString("Url")
                            ));
                        }
                        return list;
                    }
                }
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Log.e("SQLException", "listSource: ", ex);
            return null;
        }
    }

    public List<Type> listType() {
        try (Connection connection = connect()) {
            if (connection != null) {
                List<Type> list = new ArrayList<>();
                String query = "SELECT * FROM Types";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    try (ResultSet rs = statement.executeQuery()) {
                        while (rs.next()) {
                            list.add(new Type(rs.getInt("ID"), rs.getString("Name")));
                        }
                        return list;
                    }
                }
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Log.e("SQLException", "listType: ", ex);
            return null;
        }
    }

    public List<Deposit> listDeposit() {
        int id = getUserCurrent();
        if (id > 0) {
            try (Connection connection = connect()) {
                if (connection != null) {
                    List<Deposit> list = new ArrayList<>();
                    String query = "SELECT * FROM DepositHistory WHERE UserID = ? " +
                            "ORDER BY TransactionDate DESC;";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setInt(1, id);
                        try (ResultSet rs = statement.executeQuery()) {
                            while (rs.next()) {
                                list.add(new Deposit(
                                        rs.getBoolean("TransactionType"),
                                        rs.getString("Note"),
                                        rs.getInt("Amount"),
                                        rs.getTimestamp("TransactionDate"))
                                );
                            }
                            return list;
                        }
                    }
                } else {
                    return null;
                }
            } catch (SQLException ex) {
                Log.e("SQLException", "listType: ", ex);
                return null;
            }
        } else return null;
    }

    public List<Report> listReport() {
        try (Connection connection = connect()) {
            if (connection != null) {
                List<Report> list = new ArrayList<>();
                String query = "SELECT * FROM ReportTypes";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    try (ResultSet rs = statement.executeQuery()) {
                        while (rs.next()) {
                            list.add(new Report(rs.getInt("ID"), rs.getString("Name")));
                        }
                        return list;
                    }
                }
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Log.e("SQLException", "listReport: ", ex);
            return null;
        }
    }

    public List<Language> listLanguage() {
        try (Connection connection = connect()) {
            if (connection != null) {
                List<Language> list = new ArrayList<>();
                String query = "SELECT * FROM Languages";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    try (ResultSet rs = statement.executeQuery()) {
                        while (rs.next()) {
                            list.add(new Language(rs.getInt("ID"), rs.getString("Name")));
                        }
                        return list;
                    }
                }
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Log.e("SQLException", "listLanguage: ", ex);
            return null;
        }
    }

    public List<String> listLanguageName() {
        try (Connection connection = connect()) {
            if (connection != null) {
                List<String> list = new ArrayList<>();
                list.add("Tất cả");
                String query = "SELECT * FROM Languages";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    try (ResultSet rs = statement.executeQuery()) {
                        while (rs.next()) {
                            list.add(rs.getString("Name"));
                        }
                        return list;
                    }
                }
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Log.e("SQLException", "listLanguage: ", ex);
            return null;
        }
    }

    public List<Cart> listCart() {
        try (Connection connection = connect()) {
            if (connection != null) {
                int id = getUserCurrent();
                if (id > 0) {
                    List<Cart> list = new ArrayList<>();
                    String query = "SELECT c.ID, c.CodeID, sc.Name,iu.Url,sc.Fee,l.Name as Language " +
                            "FROM Carts c " +
                            "INNER JOIN SourceCodes sc ON c.CodeID = sc.ID " +
                            "INNER JOIN Languages l ON sc.LanguageID = l.ID " +
                            "OUTER APPLY (SELECT TOP 1 Url FROM ImageUrls iu WHERE iu.Source = sc.ID) iu " +
                            "WHERE c.UserID = ? AND sc.IsShow != 0 AND sc.IsDelete != 1 " +
                            "ORDER BY c.ID DESC;";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setInt(1, id);
                        try (ResultSet rs = statement.executeQuery()) {
                            while (rs.next()) {
                                list.add(new Cart(rs.getInt("ID"),
                                        rs.getInt("CodeID"),
                                        rs.getString("Name"),
                                        rs.getInt("Fee"),
                                        rs.getString("Language"),
                                        rs.getString("Url")));
                            }
                            return list;
                        }
                    }
                } else return null;
            } else return null;
        } catch (SQLException ex) {
            Log.e("SQLException", "listCart: ", ex);
            return null;
        }
    }

    public List<Order> listOrder() {
        int id = getUserCurrent();
        if (id > 0) {
            try (Connection connection = connect()) {
                if (connection != null) {
                    List<Order> list = new ArrayList<>();
                    String query = "SELECT o.CodeID, o.DateCreated, o.Fee, sc.Name, iu.Url " +
                            "FROM Orders o " +
                            "INNER JOIN SourceCodes sc ON o.CodeID = sc.ID " +
                            "OUTER APPLY (SELECT TOP 1 Url FROM ImageUrls iu WHERE iu.Source = sc.ID) iu " +
                            "WHERE o.UserID = ? " +
                            "ORDER BY o.DateCreated DESC;";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setInt(1, id);
                        try (ResultSet rs = statement.executeQuery()) {
                            while (rs.next()) {
                                list.add(new Order(
                                        rs.getString("Name"),
                                        rs.getInt("Fee"),
                                        rs.getTimestamp("DateCreated"),
                                        rs.getString("Url")
                                ));
                            }
                            return list;
                        }
                    }

                } else return null;
            } catch (SQLException ex) {
                Log.e("SQLException", "listOrder: ", ex);
                return null;
            }
        } else return null;
    }

    public List<Request> listRequest(int status) {
        int id = getUserCurrent();
        if (id > 0) {
            try (Connection connection = connect()) {
                if (connection != null) {
                    List<Request> list = new ArrayList<>();
                    String query = "SELECT * FROM Requests WHERE Requester = ? AND Status = ? " +
                            "ORDER BY CreatedDate DESC;";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setInt(1, id);
                        statement.setInt(2, status);
                        try (ResultSet rs = statement.executeQuery()) {
                            while (rs.next()) {
                                list.add(new Request(
                                        rs.getInt("ID"),
                                        rs.getString("Name"),
                                        rs.getString("Description"),
                                        rs.getInt("Status"),
                                        rs.getTimestamp("CreatedDate")
                                ));
                            }
                            return list;
                        }
                    }

                } else return null;
            } catch (SQLException ex) {
                Log.e("SQLException", "listRequest: ", ex);
                return null;
            }
        } else return null;
    }

    public Request getRequest(int id) {
        try (Connection connection = connect()) {
            if (connection != null) {
                String query = "SELECT * FROM Requests WHERE ID = ?;";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, id);
                    try (ResultSet rs = statement.executeQuery()) {
                        if (rs.next()) {
                            return new Request(
                                    rs.getInt("ID"),
                                    rs.getString("Name"),
                                    rs.getString("Description"),
                                    rs.getInt("Status"),
                                    rs.getTimestamp("CreatedDate")
                            );
                        } else return null;
                    }
                }

            } else return null;
        } catch (SQLException ex) {
            Log.e("SQLException", "listRequest: ", ex);
            return null;
        }
    }

    public List<ImageUrl> listImage(int id) {
        try (Connection connection = connect()) {
            if (connection != null) {
                List<ImageUrl> list = new ArrayList<>();
                String query = "SELECT iu.ID, iu.Url FROM SourceCodes sc JOIN ImageUrls iu ON sc.ID = iu.Source WHERE sc.ID = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, id);
                    try (ResultSet rs = statement.executeQuery()) {
                        while (rs.next()) {
                            list.add(new ImageUrl(rs.getInt("ID"), rs.getString("Url")));
                        }
                        return list;
                    }
                }
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Log.e("SQLException", "listImage: ", ex);
            return null;
        }
    }

    public Source getSource(int id) {
        try (Connection connection = connect()) {
            if (connection != null) {
                String query = "SELECT " +
                        "s. ID, " +
                        "s.Name as NameSource, " +
                        "CONCAT(m.FirstName, ' ', m.LastName) as Coder, " +
                        "s.Fee, " +
                        "t.Name as Type, " +
                        "l.Name as Language, " +
                        "s.LinkVideo, " +
                        "s.Description, " +
                        "d.Views, " +
                        "d.Purchases " +
                        "FROM SourceCodes s " +
                        "JOIN Managers m ON s.Coder = m.ID " +
                        "JOIN Types t ON s.TypeID = t.ID " +
                        "JOIN Languages l ON s.LanguageID = l.ID " +
                        "JOIN DetailCodes d ON d.Source = s.ID " +
                        "WHERE s.ID = ? AND s.IsShow != 0 AND s.IsDelete != 1";
                try (PreparedStatement statement = connection.prepareStatement(query)) {

                    statement.setInt(1, id);
                    try (ResultSet rs = statement.executeQuery()) {
                        if (rs.next()) {
                            return new Source(rs.getInt("ID"),
                                    rs.getString("NameSource"),
                                    rs.getString("Coder"),
                                    rs.getInt("Fee"),
                                    rs.getString("Type"),
                                    rs.getString("Language"),
                                    rs.getString("LinkVideo"),
                                    rs.getString("Description"),
                                    rs.getInt("Views"),
                                    rs.getInt("Purchases")
                            );
                        } else return null;
                    }
                }
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Log.e("SQLException", "getSource: ", ex);
            return null;
        }
    }

    public User getUser() {
        try (Connection connection = connect()) {
            if (connection != null) {
                int id = getUserCurrent();
                if (id > 0) {
                    String query = "SELECT *, CONCAT(FirstName, ' ', LastName) AS FullName FROM Users WHERE ID = ?";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setInt(1, id);
                        try (ResultSet rs = statement.executeQuery()) {
                            if (rs.next()) {
                                User u = new User();
                                u.setFullName(rs.getString("FullName"));
                                u.setUsername(rs.getString("Username"));
                                u.setEmail(rs.getString("Email"));
                                u.setCurrency(rs.getInt("Currency"));
                                return u;
                            } else return null;
                        }
                    }
                } else return null;

            } else return null;
        } catch (SQLException ex) {
            Log.e("SQLException", "getUser: ", ex);
            return null;
        }
    }

    public void updateViewCounter(int id) {
        try (Connection connection = connect()) {

            if (connection != null) {
                String query = "UPDATE DetailCodes SET Views = Views + 1 WHERE Source = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, id);
                    statement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            Log.e("SQLException", "updateViewCounter: ", ex);
        }
    }

    public void updatePurchases(int id) {
        try (Connection connection = connect()) {
            if (connection != null) {
                String query = "UPDATE DetailCodes SET Purchases = Purchases + 1 WHERE Source = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, id);
                    statement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            Log.e("SQLException", "updatePurchases: ", ex);
        }
    }

    public boolean addToCart(int idItem) {
        try (Connection connection = connect()) {
            if (connection != null) {
                int id = getUserCurrent();
                if (id > 0) {
                    String query = "INSERT INTO Carts (UserID, CodeID) " +
                            "SELECT u.ID, s.ID " +
                            "FROM Users u CROSS JOIN SourceCodes s " +
                            "WHERE u.ID = ? AND s.ID = ? " +
                            "AND NOT EXISTS (SELECT 1 FROM Carts c WHERE c.UserID = u.ID AND c.CodeID = s.ID);";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setInt(1, id);//User ID
                        statement.setInt(2, idItem);//Source ID
                        int status = statement.executeUpdate();
                        return status > 0;
                    }
                } else return false;
            } else return false;
        } catch (SQLException ex) {
            Log.e("SQLException", "addToCart: ", ex);
            return false;
        }
    }

    public boolean removeCart(int idCart) {
        try (Connection connection = connect()) {
            if (connection != null) {
                String query = "DELETE FROM Carts WHERE ID = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, idCart);
                    int status = statement.executeUpdate();
                    return status > 0;
                }
            } else return false;
        } catch (SQLException ex) {
            Log.e("SQLException", "removeCart: ", ex);
            return false;
        }
    }


    public int checkPayment(int idItem) {
        try (Connection connection = connect()) {
            if (connection != null) {
                int id = getUserCurrent();
                if (id > 0) {
                    String query = "SELECT CASE WHEN u.Currency >= s.Fee THEN 1 ELSE 0 END AS Result " +
                            "FROM Users u INNER JOIN SourceCodes s ON s.ID = ? WHERE u.ID = ?;";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setInt(1, idItem);//Source ID
                        statement.setInt(2, id);//User ID
                        try (ResultSet rs = statement.executeQuery()) {
                            if (rs.next()) {
                                return rs.getInt("Result");
                            } else return -1;
                        }
                    }

                } else return -1;

            } else return -1;
        } catch (SQLException ex) {
            Log.e("SQLException", "checkPayment: ", ex);
            return -1;
        }
    }

    public boolean postRequest(Request request) {
        int id = getUserCurrent();
        if (id > 0) {
            try (Connection connection = connect()) {
                if (connection != null) {
                    String query = "INSERT INTO Requests (Name, Description, Requester, Status) VALUES (?, ?, ?, ?);";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setString(1, request.getName());
                        statement.setString(2, request.getDesc());
                        statement.setInt(3, id);
                        statement.setInt(4, 0);
                        int status = statement.executeUpdate();
                        return status > 0;
                    }
                } else return false;
            } catch (SQLException ex) {
                Log.e("SQLException", "postRequest: ", ex);
                return false;
            }
        } else return false;
    }

    public boolean postReport(int reportType, int idSource) {
        try (Connection connection = connect()) {
            if (connection != null) {
                int id = getUserCurrent();
                if (id > 0) {
                    String query = "INSERT INTO Reports (ReportTypeID, Reporter, Source) VALUES (?, ?, ?);";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setInt(1, reportType);
                        statement.setInt(2, id);
                        statement.setInt(3, idSource);
                        int status = statement.executeUpdate();
                        return status > 0;
                    }
                } else return false;
            } else return false;
        } catch (SQLException ex) {
            Log.e("SQLException", "postReport: ", ex);
            return false;
        }
    }

    public boolean progressPayment(int codeID, int fee) {
        Connection connection = connect();
        try {
            if (connection != null) {
                int userID = getUserCurrent();
                if (userID > 0) {
                    connection.setAutoCommit(false);
                    String orderQuery = "INSERT INTO Orders (UserID, CodeID, Fee) VALUES (?, ?, ?)";
                    String detailCodeQuery = "UPDATE DetailCodes SET Purchases = Purchases + 1 WHERE Source = ?";
                    String userCurrencyQuery = "UPDATE Users SET Currency = Currency - ? WHERE ID = ?";
                    String depositQuery = "INSERT INTO DepositHistory (UserID, Amount, TransactionType, Note) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement orderStmt = connection.prepareStatement(orderQuery);
                         PreparedStatement detailCodeStmt = connection.prepareStatement(detailCodeQuery);
                         PreparedStatement userCurrencyStmt = connection.prepareStatement(userCurrencyQuery);
                         PreparedStatement depositStmt = connection.prepareStatement(depositQuery)) {

                        // Thêm đơn hàng mới vào bảng Orders
                        orderStmt.setInt(1, userID);
                        orderStmt.setInt(2, codeID);
                        orderStmt.setInt(3, fee);
                        orderStmt.executeUpdate();

                        // Tăng lượt bán cho mã nguồn tương ứng
                        detailCodeStmt.setInt(1, codeID);
                        detailCodeStmt.executeUpdate();

                        // Trừ xu của người dùng
                        userCurrencyStmt.setInt(1, fee);
                        userCurrencyStmt.setInt(2, userID);
                        userCurrencyStmt.executeUpdate();

                        //Lịch sử giao dịch
                        depositStmt.setInt(1, userID);
                        depositStmt.setInt(2, fee);
                        depositStmt.setBoolean(3, false);
                        depositStmt.setString(4, "Thanh toán mã nguồn");
                        depositStmt.executeUpdate();

                        connection.commit();
                        return true;
                    }
                } else return false;
            } else return false;
        } catch (SQLException ex) {
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException rollbackEx) {
                Log.e("SQLException", "progressPayment - Rollback Error: ", rollbackEx);
            }
            Log.e("SQLException", "progressPayment: ", ex);
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException closeEx) {
                Log.e("SQLException", "progressPayment - Close Connection Error: ", closeEx);
            }
        }
    }

    public String getLinkDownload(int id) {
        try (Connection connection = connect()) {
            if (connection != null) {
                String query = "SELECT SourceLink FROM SourceCodes WHERE ID = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, id);
                    try (ResultSet rs = statement.executeQuery()) {
                        if (rs.next()) {
                            return rs.getString("SourceLink");
                        } else return "";
                    }
                }
            } else return "";
        } catch (SQLException ex) {
            Log.e("SQLException", "getLinkDownload: ", ex);
            return "";
        }
    }

    public void rechargeCoins(int fee) {
        int id = getUserCurrent();
        if (id > 0) {
            try (Connection connection = connect()) {
                if (connection != null) {
                    connection.setAutoCommit(false);
                    String currencyQuery = "UPDATE Users SET Currency = Currency + ? WHERE ID = ?";
                    String depositQuery = "INSERT INTO DepositHistory (UserID, Amount, TransactionType, Note) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement currencyStmt = connection.prepareStatement(currencyQuery);
                         PreparedStatement depositStmt = connection.prepareStatement(depositQuery)) {
                        currencyStmt.setInt(1, fee);
                        currencyStmt.setInt(2, id);
                        currencyStmt.executeUpdate();

                        depositStmt.setInt(1, id);
                        depositStmt.setInt(2, fee);
                        depositStmt.setBoolean(3, true);
                        depositStmt.setString(4, "Nạp xu");
                        depositStmt.executeUpdate();

                        connection.commit();
                    } catch (SQLException ex) {
                        connection.rollback();
                        Log.e("SQLException", "rechargeCoins: ", ex);
                    } finally {
                        connection.setAutoCommit(true);
                    }
                }
            } catch (SQLException ex) {
                Log.e("SQLException", "rechargeCoins: ", ex);
            }
        }
    }
}

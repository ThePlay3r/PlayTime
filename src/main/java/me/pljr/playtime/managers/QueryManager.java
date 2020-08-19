package me.pljr.playtime.managers;

import me.pljr.playtime.PlayTime;
import me.pljr.playtime.objects.CorePlayer;
import me.pljr.pljrapi.database.DataSource;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class QueryManager {
    private final PlayTime instance = PlayTime.getInstance();
    private final DataSource dataSource;

    public QueryManager(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public void loadPlayerSync(UUID uuid){
        try {
            long yesterday = 0;
            long daily = 0;
            long weekly = 0;
            long monthly = 0;
            long all = 0;

            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM playtime_players WHERE uuid=?"
            );
            preparedStatement.setString(1, uuid.toString());
            ResultSet results = preparedStatement.executeQuery();
            if (results.next()){
                yesterday = results.getLong("yesterday");
                daily = results.getLong("daily");
                weekly = results.getLong("weekly");
                monthly = results.getLong("monthly");
                all = results.getLong("alltime");
            }
            dataSource.close(connection, preparedStatement, results);
            PlayTime.getPlayerManager().setCorePlayer(uuid, new CorePlayer(yesterday, daily, weekly, monthly, all));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void loadPlayer(UUID uuid){
        Bukkit.getScheduler().runTaskAsynchronously(instance, ()->{
            try {
                long yesterday = 0;
                long daily = 0;
                long weekly = 0;
                long monthly = 0;
                long all = 0;

                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM playtime_players WHERE uuid=?"
                );
                preparedStatement.setString(1, uuid.toString());
                ResultSet results = preparedStatement.executeQuery();
                if (results.next()){
                    yesterday = results.getLong("yesterday");
                    daily = results.getLong("daily");
                    weekly = results.getLong("weekly");
                    monthly = results.getLong("monthly");
                    all = results.getLong("alltime");
                }
                dataSource.close(connection, preparedStatement, results);
                PlayTime.getPlayerManager().setCorePlayer(uuid, new CorePlayer(yesterday, daily, weekly, monthly, all));
            }catch (SQLException e){
                e.printStackTrace();
            }
        });
    }

    public void savePlayerSync(UUID uuid){
        CorePlayer corePlayer = PlayTime.getPlayerManager().getCorePlayer(uuid);
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "REPLACE INTO playtime_players VALUES (?,?,?,?,?,?)"
            );
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setLong(2, corePlayer.getYesterday());
            preparedStatement.setLong(3, corePlayer.getDaily());
            preparedStatement.setLong(4, corePlayer.getWeekly());
            preparedStatement.setLong(5, corePlayer.getMontly());
            preparedStatement.setLong(6, corePlayer.getAll());
            preparedStatement.executeUpdate();
            dataSource.close(connection, preparedStatement, null);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void savePlayer(UUID uuid){
        CorePlayer corePlayer = PlayTime.getPlayerManager().getCorePlayer(uuid);
        Bukkit.getScheduler().runTaskAsynchronously(instance, ()->{
            try {
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "REPLACE INTO playtime_players VALUES (?,?,?,?,?,?)"
                );
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.setLong(2, corePlayer.getYesterday());
                preparedStatement.setLong(3, corePlayer.getDaily());
                preparedStatement.setLong(4, corePlayer.getWeekly());
                preparedStatement.setLong(5, corePlayer.getMontly());
                preparedStatement.setLong(6, corePlayer.getAll());
                preparedStatement.executeUpdate();
                dataSource.close(connection, preparedStatement, null);
            }catch (SQLException e){
                e.printStackTrace();
            }
        });
    }

    public void updateDates(){
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM playtime_dates"
            );
            ResultSet results = preparedStatement.executeQuery();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = new Date();

            Calendar cDaily = Calendar.getInstance();
            cDaily.setTime(currentDate);
            cDaily.add(Calendar.DATE, 1);
            Calendar cWeekly = Calendar.getInstance();
            cWeekly.setTime(currentDate);
            cWeekly.add(Calendar.DATE, 7);
            Calendar cMontlhy = Calendar.getInstance();
            cMontlhy.setTime(currentDate);
            cMontlhy.add(Calendar.DATE, 30);

            String daily;
            String weekly;
            String monthly;

            if (results.next()){
                Date dailyDate = sdf.parse(results.getString("daily"));
                Date weeklyDate = sdf.parse(results.getString("weekly"));
                Date monthlyDate = sdf.parse(results.getString("monthly"));
                if (currentDate.compareTo(dailyDate) >= 0){
                    daily = sdf.format(cDaily.getTime());

                    Connection update = dataSource.getConnection();
                    PreparedStatement updateStatement = update.prepareStatement(
                            "UPDATE playtime_players SET yesterday=daily"
                    );
                    updateStatement.executeUpdate();
                    dataSource.close(update, updateStatement, null);

                    Connection clear = dataSource.getConnection();
                    PreparedStatement clearStatement = clear.prepareStatement(
                            "UPDATE playtime_players SET daily=0"
                    );
                    clearStatement.executeUpdate();
                    dataSource.close(clear, clearStatement, null);
                }else{
                    daily = sdf.format(dailyDate.getTime());
                }
                if (currentDate.compareTo(weeklyDate) >= 0){
                    weekly = sdf.format(cWeekly.getTime());

                    Connection clear = dataSource.getConnection();
                    PreparedStatement clearStatement = clear.prepareStatement(
                            "UPDATE playtime_players SET weekly=0"
                    );
                    clearStatement.executeUpdate();
                    dataSource.close(clear, clearStatement, null);
                }else{
                    weekly = sdf.format(weeklyDate.getTime());
                }
                if (currentDate.compareTo(monthlyDate) >= 0){
                    monthly = sdf.format(cMontlhy);

                    Connection clear = dataSource.getConnection();
                    PreparedStatement clearStatement = clear.prepareStatement(
                            "UPDATE playtime_players SET monthly=0"
                    );
                    clearStatement.executeUpdate();
                    dataSource.close(clear, clearStatement, null);
                }else{
                    monthly = sdf.format(monthlyDate.getTime());
                }
            }else{
                daily = sdf.format(cDaily.getTime());
                weekly = sdf.format(cWeekly.getTime());
                monthly = sdf.format(cMontlhy.getTime());
            }
            dataSource.close(connection, preparedStatement, results);

            Connection remove = dataSource.getConnection();
            PreparedStatement removeStatement = remove.prepareStatement(
                    "DELETE FROM playtime_dates"
            );
            removeStatement.executeUpdate();
            dataSource.close(remove, removeStatement, null);

            Connection insert = dataSource.getConnection();
            PreparedStatement insertStatement = insert.prepareStatement(
                    "INSERT INTO playtime_dates VALUES (?,?,?)"
            );
            insertStatement.setString(1, daily);
            insertStatement.setString(2, weekly);
            insertStatement.setString(3, monthly);
            insertStatement.executeUpdate();
            dataSource.close(insert, insertStatement, null);

        }catch (SQLException | ParseException e){
            e.printStackTrace();
        }
    }

    public void setupTables(){
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS playtime_players (" +
                            "uuid char(36) NOT NULL PRIMARY KEY," +
                            "yesterday bigint(20) NOT NULL," +
                            "daily bigint(20) NOT NULL," +
                            "weekly bigint(20) NOT NULL," +
                            "monthly bigint(20) NOT NULL," +
                            "alltime bigint(20) NOT NULL);"
            );
            preparedStatement.executeUpdate();
            dataSource.close(connection, preparedStatement, null);

            Connection connection2 = dataSource.getConnection();
            PreparedStatement preparedStatement2 = connection2.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS playtime_dates (" +
                            "daily char(10) NOT NULL," +
                            "weekly char(10) NOT NULL," +
                            "monthly char(10) NOT NULL);"
            );
            preparedStatement2.executeUpdate();
            dataSource.close(connection2, preparedStatement2, null);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}

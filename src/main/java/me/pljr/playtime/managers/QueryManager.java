package me.pljr.playtime.managers;

import me.pljr.playtime.PlayTime;
import me.pljr.playtime.objects.CorePlayer;
import me.pljr.pljrapi.database.DataSource;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                    "SELEC * FROM playtime_players WHERE uuid=?"
            );
            preparedStatement.setString(1, uuid.toString());
            ResultSet results = preparedStatement.executeQuery();
            if (results.next()){
                yesterday = results.getLong("yesterday");
                daily = results.getLong("daily");
                weekly = results.getLong("weekly");
                monthly = results.getLong("monthly");
                all = results.getLong("all");
            }
            dataSource.close(connection, preparedStatement, results);
            PlayerManager.setCorePlayer(uuid, new CorePlayer(yesterday, daily, weekly, monthly, all));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void savePlayer(UUID uuid){
        CorePlayer corePlayer = PlayerManager.getCorePlayer(uuid);
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
                            "all bigint(20) NOT NULL);"
            );
            preparedStatement.executeUpdate();
            dataSource.close(connection, preparedStatement, null);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}

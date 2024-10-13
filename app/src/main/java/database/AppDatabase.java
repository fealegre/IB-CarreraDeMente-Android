package database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import dao.DaoCategoria;
import dao.DaoPregunta;
import dao.DaoUsuario;
import model.Categoria;
import model.Pregunta;
import model.Usuario;

@Database(
        entities = {Usuario.class, Pregunta.class, Categoria.class},
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {

    public static AppDatabase INSTANCE;

    public abstract DaoUsuario daoUsuario();

    public abstract DaoPregunta daoPregunta();

    public abstract DaoCategoria daoCategoria();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "dbTrivia").allowMainThreadQueries().build();
        }
        INSTANCE.getOpenHelper().getWritableDatabase();
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


}

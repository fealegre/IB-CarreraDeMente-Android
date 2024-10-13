package dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import model.Usuario;

@Dao
public interface DaoUsuario {

    @Insert
    public void insertarUsuario(Usuario usuario );

    @Query("SELECT * FROM usuarios WHERE dni = :dni")
    public Usuario buscarUsuarioPorDni(String dni);

    @Update
    public void actualizarUsuario(Usuario usuario);

    @Delete
    public void borrarUsuario(Usuario usuario);

    @Query("SELECT * FROM usuarios")
    public List<Usuario> listarUsuarios();

}

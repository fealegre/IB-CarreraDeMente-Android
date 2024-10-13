package dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import model.Categoria;

@Dao
public interface DaoCategoria {

    @Insert
    void agregarCategoria(Categoria categoria);

    @Query("SELECT * FROM categorias")
    List<Categoria> obtenerCategorias();

    @Query("SELECT * FROM categorias WHERE id = :id")
    Categoria obtenerCategoriaPorId(int id);


}

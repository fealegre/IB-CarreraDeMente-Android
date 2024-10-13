package dao;

import androidx.room.Dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import model.Pregunta;

@Dao
public interface DaoPregunta {

    @Query("SELECT * FROM preguntas")
    List<Pregunta> listarPreguntas();

//    @Query("SELECT * FROM preguntas WHERE id = :id")
//    Pregunta buscarPreguntaPorId(int id);

    @Query("SELECT * FROM preguntas WHERE nivel = :nivel")
    List<Pregunta> buscarPreguntasPorNivel(int nivel);

    @Insert
    void insertarPregunta(Pregunta pregunta);

    @Update
    void actualizarPregunta(Pregunta pregunta);

    @Delete
    void eliminarPregunta(Pregunta pregunta);

    @Delete
    void eliminarPreguntas(List<Pregunta> preguntas);

    @Query("DELETE FROM sqlite_sequence WHERE name = 'preguntas'")
    void eliminarPrimaryKeyIndex();

}

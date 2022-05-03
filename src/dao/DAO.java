package dao;

import model.CampoSQL;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public abstract class DAO<T extends Record> {
    public DataSource dataSource;

     public DAO(DataSource ds) {
        this.dataSource = ds;
    }

    public abstract ArrayList<T> lerTudo() throws SQLException;
    public abstract void inserir(T obj) throws SQLException;
    public abstract void alterar(T obj) throws SQLException;
    public abstract void deletar(int id) throws SQLException;

    public abstract Class<?> tipoRegistro();

    public final String[] colunas() {
        Field[] campos = tipoRegistro().getDeclaredFields();
        String[] cols = new String[campos.length];

        for (int i = 0; i < campos.length; i++) {
            Field campo = campos[i];
            CampoSQL an = campo.getAnnotation(CampoSQL.class);
            if (an == null){
                cols[i] = null;
                continue;
            }
            String col = an.nomeColuna();
            if (Objects.equals(col, "")) {
                col = campo.getName();
                col = col.substring(0, 1).toUpperCase() + col.substring(1);
            }
            cols[i] = col;
        }

        return cols;
    }
    public final Object lerValor(Object obj, int col) throws IllegalAccessException {
        return obj.getClass().getDeclaredFields()[col].get(obj);
    }

    public static String[] tabelas = new String[] {"Municipio", "Categoria de reporte"};
    public static DAO<?> criar(String tabela, DataSource ds) {
        return switch (tabela) {
            case "Municipio" -> new DAOMunicipio(ds);
            case "Categoria de reporte" -> new DAOCategoriaReporte(ds);
            default -> throw new IllegalArgumentException("Nome de tabela invalido.");
        };
    }
}
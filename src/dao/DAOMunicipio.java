package dao;

import model.Municipio;
import util.Estado;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAOMunicipio extends DAO<Municipio> {
    public DAOMunicipio(DataSource ds) {
        super(ds);
    }

    Municipio lerRegistro(ResultSet rs) throws SQLException {
        return new Municipio(
                rs.getInt("id_municipio"),
                rs.getString("nome"),
                Estado.valueOf(rs.getString("estado"))
        );
    }
    public ArrayList<Municipio> lerTudo() throws SQLException {
        try (PreparedStatement ps = dataSource.preparar("select * from municipio"); ResultSet rs = ps.executeQuery()) {
            ArrayList<Municipio> lista = new ArrayList<>();
            while (rs.next()) lista.add(lerRegistro(rs));
            return lista;
        }
    }
    public void inserir(Municipio m) throws SQLException {
        try (PreparedStatement ps = dataSource.preparar(
                "insert into municipio (nome, estado) values (?, ?)",
                m.nome(),
                m.estado().name()
        )) {}
    }
    public void alterar(Municipio m) throws SQLException {
        try (PreparedStatement ps = dataSource.preparar(
                "update municipio set nome=?, estado=? where id_municipio=?",
                m.nome(),
                m.estado().name(),
                String.valueOf(m.idMunicipio())
        )) {}
    }
    public void deletar(int codigo) throws SQLException {
        try (PreparedStatement ps = dataSource.preparar(
                "delete from municipio where (id_municipio = ?)",
                String.valueOf(codigo)
        )) {}
    }

    public Class<?> tipoRegistro() {
        return Municipio.class;
    }
}
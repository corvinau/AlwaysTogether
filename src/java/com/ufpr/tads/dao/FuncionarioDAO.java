/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.tads.dao;

import com.ufpr.tads.beans.Funcionario;
import com.ufpr.tads.beans.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ArtVin
 */
public class FuncionarioDAO {
    private Connection con;
    private ResultSet rs;
    
    public FuncionarioDAO(){
        try {
            con = ConnectionFactory.getConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public FuncionarioDAO(Connection con){
        this.con = con;
    }
    
    public Funcionario getFuncionario(Usuario u){
        Funcionario f = null;
        PreparedStatement st;
        
        try {
            st = con.prepareStatement(
                      "SELECT idFuncionario, nome, cargo, cpf, "
                    + "dataNascimento, Endereco_idEndereco FROM funcionario "
                    + "WHERE Usuario_idUsuario = ? "
            );
            st.setInt(1, u.getIdUsuario());
            EnderecoDAO enderecoDAO = new EnderecoDAO(con);
            rs = st.executeQuery();
            while(rs.next()){
                f = new Funcionario();
                f.setIdUsuario(u.getIdUsuario());
                f.setEmail(getEmailFuncionario(u.getIdUsuario()));
                f.setIdFuncionario(rs.getInt("idFuncionario"));
                f.setNome(rs.getString("nome"));
                f.setCargo(rs.getString("cargo"));
                f.setCpf(rs.getString("cpf"));
                f.setDataNasc(rs.getDate("dataNascimento"));
                f.setEndereco(enderecoDAO.getEndereco(rs.getInt("Endereco_idEndereco")));
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        return f;
    }

    public int insertFuncionario(Funcionario f) {
        PreparedStatement st;
        int aux;
        try {
            st = con.prepareStatement(
                      "INSERT INTO Funcionario(nome, cargo, cpf, "
                    + "dataNascimento, Endereco_idEndereco, Usuario_idUsuario) "
                    + "VALUES(?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS
            );
            st.setString(1, f.getNome());
            st.setString(2, f.getCargo());
            st.setString(3, f.getCpf());
            st.setDate(4, new java.sql.Date(f.getDataNasc().getTime()));
            if(f.getEndereco() != null){
                if(f.getEndereco().getIdEndereco() == 0){
                    EnderecoDAO enderecoDAO = new EnderecoDAO(con);
                    aux = enderecoDAO.insertEndereco(f.getEndereco());
                    f.getEndereco().setIdEndereco(aux);
                    st.setInt(5, f.getEndereco().getIdEndereco());
                }
                else st.setNull(5, java.sql.Types.INTEGER);
            }
            else{
                st.setNull(5, java.sql.Types.INTEGER);
            }
            st.setInt(6, f.getIdUsuario());
            
            st.executeUpdate();
            
            rs = st.getGeneratedKeys();
            if(rs.next()) return rs.getInt(1);
            
            
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        return 0;
    }
    public boolean updateFuncionario(Funcionario f) {
        PreparedStatement st;
        int aux;
        try {
            st = con.prepareStatement(
                      "UPDATE Funcionario SET nome = ?, cargo = ?, "
                    + "Endereco_idEndereco = ? WHERE idFuncionario = ?"
            );
            
            st.setString(1, f.getNome());
            st.setString(2, f.getCargo());
            if(f.getEndereco() != null){
                EnderecoDAO enderecoDAO = new EnderecoDAO(con);
                if(f.getEndereco().getIdEndereco() == 0){
                    aux = enderecoDAO.insertEndereco(f.getEndereco());
                    f.getEndereco().setIdEndereco(aux);
                }
                else {
                    enderecoDAO.updateEndereco(f.getEndereco());
                }
                st.setInt(3, f.getEndereco().getIdEndereco());
            }
            else{
                st.setNull(3, java.sql.Types.INTEGER);
            }
            st.setInt(4, f.getIdFuncionario());
            
            aux = st.executeUpdate();
            if(aux > 0) return true;
            
            
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        return true;
    }

    public List<Funcionario> getListaFuncionario() {
        List<Funcionario> lista = new ArrayList<Funcionario>();
        Funcionario f;
        PreparedStatement st;
        
        try {
            st = con.prepareStatement(
                      "SELECT F.idFuncionario, F.nome, F.cargo, F.cpf, "
                    + "F.dataNascimento, U.email FROM funcionario F "
                    + "INNER JOIN usuario U ON U.idUsuario = F.Usuario_idUsuario "
            );
            EnderecoDAO enderecoDAO = new EnderecoDAO(con);
            rs = st.executeQuery();
            while(rs.next()){
                f = new Funcionario();
                f.setIdFuncionario(rs.getInt("F.idFuncionario"));
                f.setNome(rs.getString("F.nome"));
                f.setCargo(rs.getString("F.cargo"));
                f.setCpf(rs.getString("F.cpf"));
                f.setDataNasc(rs.getDate("F.dataNascimento"));
                f.setEmail(rs.getString("U.email"));
                lista.add(f);
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        return lista;
    }

    private String getEmailFuncionario(int idUsuario) {
        PreparedStatement st;
        ResultSet res;
        String email = null;
        try {
            st = con.prepareStatement("SELECT EMAIL FROM USUARIO WHERE IDUSUARIO = ?;");
                st.setInt(1, idUsuario);
                res = st.executeQuery();
            while(res.next()){
                email = res.getString(1);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }   
        return email;
    }

    public void removeFuncionario(int idFuncionario) {
        PreparedStatement st;
        try {
            st = con.prepareStatement("DELETE FROM USUARIO WHERE IDUSUARIO = ?;");
                st.setInt(1, getIdUsuario(idFuncionario));
                st.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            st = con.prepareStatement("DELETE FROM funcionario WHERE idFuncionario = ?;");
                st.setInt(1, idFuncionario);
                st.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int getIdUsuario(int idFuncionario) {
        PreparedStatement st;
        ResultSet res;
        int idUsuario = 0;
        try {
            st = con.prepareStatement("SELECT Usuario_idUsuario FROM funcionario WHERE idFuncionario = ?;");
                st.setInt(1, idFuncionario);
                res = st.executeQuery();
            while(res.next()){
                idUsuario = res.getInt(1);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }   
        return idUsuario;
    }
}

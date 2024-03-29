package com.hhkysely.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.hhkysely.objects.Kysely;
import com.hhkysely.objects.Kysymys;




@Repository
public class KyselyDAOSpringJdbcImpl implements KyselyDAO {

	@Inject
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	
	/**
	 * Tallettaa parametrina annetun henkilön tietokantaan.
	 * Tietokannan generoima id asetetaan parametrina annettuun olioon.
	 */
	public void talleta(Kysymys k, int kyselyid) {
		final String sql = "insert into kysymys(teksti,kyselyid,tyyppiid) values(?,?,?)";
		
		//anonyymi sisäluokka tarvitsee vakioina välitettävät arvot,
		//jotta roskien keruu onnistuu tämän metodin suorituksen päättyessää. 
		final String teksti = k.getTeksti();
		final int tyyppiid = k.getTyyppiid();
		final int kysely = kyselyid;
		//final Tyyppi tyyppi = k.getTyyppi();
		
		
		//jdbc pistää generoidun id:n tänne talteen
		KeyHolder idHolder = new GeneratedKeyHolder();
	    
		//suoritetaan päivitys itse määritellyllä PreparedStatementCreatorilla ja KeyHolderilla
		jdbcTemplate.update(
	    	    new PreparedStatementCreator() {
	    	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	    	            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
	    	            ps.setString(1, teksti);
	    	            ps.setInt(2, kysely);
	    	            ps.setInt(3, tyyppiid);
	    	            return ps;
	    	        }
	    	    }, idHolder);
	    
		//tallennetaan id takaisin beaniin, koska
		//kutsujalla pitäisi olla viittaus samaiseen olioon
	    k.setId(idHolder.getKey().intValue());

	}

	@Override
	public Kysely haeKysely(int id) throws Exception {
		String sql = "SELECT * FROM kysely INNER JOIN kysymys ON kysely.kyselyid=kysymys.kyselyid LEFT JOIN vaihtoehto ON kysymys.kysymysid=vaihtoehto.kysymysid WHERE kysely.kyselyid=? ORDER BY kysymys.kysymysid;";
		Object[] parametrit = new Object[] { id };
		ResultSetExtractor<Kysely> extractor = new YksiKyselyExtractor();
		
	    Kysely k;
	    try { 
	    	k = jdbcTemplate.query(sql, parametrit, extractor);
	    } catch(IncorrectResultSizeDataAccessException e) {
	    	throw new Exception(e);
	    }
	    return k;
	              
	}
	
//SELECT * FROM kysely INNER JOIN kysymys ON kysely.kyselyid=kysymys.kyselyid LEFT JOIN vaihtoehto ON kysymys.kysymysid=vaihtoehto.kysymysid WHERE kysely.kyselyid=1 ORDER BY kysymys.kysymysid;


	

	public void talletaKysely(Kysely k) {
		final String sql = "insert into kysely(nimi,tyyppi,tila,alkamispvm,luontipvm) values(?,?,?,'','')";
		//anonyymi sisäluokka tarvitsee vakioina välitettävät arvot,
		//jotta roskien keruu onnistuu tämän metodin suorituksen päättyessää. 
		final String nimi = k.getNimi();
		final String tyyppi = k.getTyyppi();
		final String tila = k.getTila();
		
		//jdbc pistää generoidun id:n tänne talteen
		KeyHolder idHolder = new GeneratedKeyHolder();
	    
		//suoritetaan päivitys itse määritellyllä PreparedStatementCreatorilla ja KeyHolderilla
		jdbcTemplate.update(
	    	    new PreparedStatementCreator() {
	    	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	    	            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
	    	            ps.setString(1, nimi);
	    	            ps.setString(2, tyyppi);
	    	            ps.setString(3, tila);
	    	            return ps;
	    	        }
	    	    }, idHolder);
	    
		//tallennetaan id takaisin beaniin, koska
		//kutsujalla pitäisi olla viittaus samaiseen olioon
	    k.setId(idHolder.getKey().intValue());

	}

	
	/*public int etsiKyselyID(String nimi) {
		Connection connection;
		String selectSQL = "select id from kysely where nimi = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
		preparedStatement.setInt(1, 1001);
		ResultSet rs = preparedStatement.executeQuery(selectSQL );
		int id = rs.getInt("id");
		return id;

	}*/

	
	
}

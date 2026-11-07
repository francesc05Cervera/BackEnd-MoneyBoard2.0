package com.MoneyBoard2.Repository;

import com.MoneyBoard2.Model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {

	Utente findByUsername(String username);
	Utente findByemail(String email);
	boolean existsByEmail(String email);
}
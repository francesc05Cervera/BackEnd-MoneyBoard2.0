package com.MoneyBoard2.Repository;

import com.MoneyBoard2.Model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {

	Utente findByUsername(String username);
}
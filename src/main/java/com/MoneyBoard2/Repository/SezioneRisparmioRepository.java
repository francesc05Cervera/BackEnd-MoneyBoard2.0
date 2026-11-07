package com.MoneyBoard2.Repository;

import com.MoneyBoard2.Model.SezioneRisparmio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SezioneRisparmioRepository extends JpaRepository<SezioneRisparmio, Long> {
	List<SezioneRisparmio> findByUtenteId(Long utenteId);
	SezioneRisparmio findBynome(String nome);
	boolean deleteByNome(String nome); 
	boolean existsByNome(String nome);
}
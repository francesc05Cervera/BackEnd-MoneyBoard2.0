package com.MoneyBoard2.Repository;

import com.MoneyBoard2.Model.Transazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransazioneRepository extends JpaRepository<Transazione, Long> {
    
    List<Transazione> findByUtenteId(Long utenteId);
    
    List<Transazione> findBySezioneId(Long sezioneId);
    
    List<Transazione> findByUtenteIdAndSezioneId(Long utenteId, Long sezioneId);
}
package com.project.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Entity
@Table(name="projekt")
public class Projekt {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="projekt_id") 
private Integer projektId;
	
	@Column(nullable = false, length = 1000)
private String nazwa;
	
	@Column(length = 50)
private String opis;
	
	@CreationTimestamp
	@Column(name = "dataczas_utworzenia", nullable = false, updatable = false)
private LocalDateTime dataCzasUtworzenia;
	
private LocalDate dataOddania;

@ManyToMany 
@JoinTable(name = "projekt_student",
joinColumns = {@JoinColumn(name="projekt_id")},
inverseJoinColumns = {@JoinColumn(name="student_id")}) 
private Set<Student> studenci;

//	@UpdateTimestamp
//	@Column(name = "dataczas_modyfikacji", nullable = false)
//private LocalDateTime dataCzasModyfikacji;

public Projekt(Integer projektId, String nazwa, String opis, LocalDateTime dataCzasUtworzenia, LocalDate dataOddania) {
	super();
	this.projektId = projektId;
	this.nazwa = nazwa;
	this.opis = opis;
	this.dataCzasUtworzenia = dataCzasUtworzenia;
	this.dataOddania = dataOddania;
}


public Projekt(String nazwa, String opis, LocalDate dataOddania) {
	super();
	this.nazwa = nazwa;
	this.opis = opis;
	this.dataOddania = dataOddania;
}


public Projekt() {
	
}

public Integer getProjektId() {
	return projektId;
}

public void setProjektId(Integer projektId) {
	this.projektId = projektId;
}

public String getNazwa() {
	return nazwa;
}

public void setNazwa(String nazwa) {
	this.nazwa = nazwa;
}

public String getOpis() {
	return opis;
}

public void setOpis(String opis) {
	this.opis = opis;
}

public LocalDateTime getDataCzasUtworzenia() {
	return dataCzasUtworzenia;
}

public void setDataCzasUtworzenia(LocalDateTime dataCzasUtworzenia) {
	this.dataCzasUtworzenia = dataCzasUtworzenia;
}

public LocalDate getDataOddania() {
	return dataOddania;
}

public void setDataOddania(LocalDate dataOddania) {
	this.dataOddania = dataOddania;
}

/* TODO Wygeneruj dla powy¿szych zmiennych akcesory (Source -> Generate Getters and Setters).
* Dodaj równie¿ bezparametrowy konstruktor oraz drugi konstruktor uwzglêdniaj¹cy wszystkie powy¿sze
* zmienne, a tak¿e trzeci pomijaj¹cy pola projektId oraz dataCzasUtworzenia.
*/

}
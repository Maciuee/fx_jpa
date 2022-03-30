package com.project.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="zadanie")
public class Zadanie {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name="zadanie_id")
private Integer zadanieId;

@ManyToOne
@JoinColumn(name = "projekt_id")
private Projekt projekt;

@Column(nullable = false)
private String nazwa;

private Integer kolejnosc;

private String opis;
@Column(nullable = false)
private LocalDate dataczas_Oddania;


public Zadanie(Integer zadanieId, Projekt projekt, String nazwa, Integer kolejnosc, String opis,
		LocalDate dataczas_Oddania) {
	super();
	this.zadanieId = zadanieId;
	this.projekt = projekt;
	this.nazwa = nazwa;
	this.kolejnosc = kolejnosc;
	this.opis = opis;
	this.dataczas_Oddania = dataczas_Oddania;
}
public Zadanie() {
	
}
public Integer getZadanieId() {
	return zadanieId;
}
public void setZadanieId(Integer zadanieId) {
	this.zadanieId = zadanieId;
}
public Projekt getProjekt() {
	return projekt;
}
public void setProjekt(Projekt projekt) {
	this.projekt = projekt;
}
public String getNazwa() {
	return nazwa;
}
public void setNazwa(String nazwa) {
	this.nazwa = nazwa;
}
public Integer getKolejnosc() {
	return kolejnosc;
}
public void setKolejnosc(Integer kolejnosc) {
	this.kolejnosc = kolejnosc;
}
public String getOpis() {
	return opis;
}
public void setOpis(String opis) {
	this.opis = opis;
}
public LocalDate getDataczas_Oddania() {
	return dataczas_Oddania;
}
public void setDataczas_Oddania(LocalDate dataczas_Oddania) {
	this.dataczas_Oddania = dataczas_Oddania;
}


/*TODO Uzupe³nij kod o zmienne reprezentuj¹ce pozosta³e pola tabeli zadanie (patrz rys. 8.1), 
. nastêpnie wygeneruj dla nich akcesory (Source -> Generate Getters and Setters),
. ponadto dodaj pusty konstruktor oraz konstruktor ze zmiennymi nazwa, opis i kolejnosc.
*/
}


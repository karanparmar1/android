package com.k5207.jodidar.model;

import java.sql.Date;
import java.util.Calendar;

public class User {

	int id;
	String firstname, lastname, email, password, gender;
	Date bday;
	String religion, caste, location, education, profession, mothertongue;
	Double height;
	String eating;
	String manglik;
	String lookingfor, about, image;
	int online;
	int age;

	/*
	 * //All records inorder of returned "resultData" from php
	 * ($id,$firstname,$lastname,$email,$password,$bday,$gender,$religion,$caste,
	 * $location,$education,
	 * $profession,$mothertongue,$height,$eating,$manglik,$lookingfor,$about,$image,
	 * $online);
	 */

	public User() {
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", email=" + email + ", gender="
				+ gender + ", bday=" + bday + ", religion=" + religion + ", caste=" + caste + ", location=" + location
				+ ", education=" + education + ", profession=" + profession + ", mothertongue=" + mothertongue + ", height="
				+ height + ", eating=" + eating + ", manglik=" + manglik + ", lookingfor=" + lookingfor + ", about=" + about
				+ ", image=" + image + ", online=" + online + ", age=" + age + "]";
	}

	public User(int id, String firstname, String lastname, String email, String password, Date bday, String gender,
			String religion, String caste, String location, String education, String profession, String mothertongue,
			Double height, String eating, String manglik, String lookingfor, String about, String image, int online) {

		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		this.bday = bday;
		this.gender = gender;
		this.religion = religion;
		this.caste = caste;
		this.location = location;
		this.education = education;
		this.profession = profession;
		this.mothertongue = mothertongue;
		this.height = height;
		this.eating = eating;
		this.manglik = manglik;
		this.lookingfor = lookingfor;
		this.about = about;
		this.image = image;
		this.online = online;
	}

	// ForOther User Without Password
	public User(int id, String firstname, String lastname, String email, Date bday, String gender, String religion, String caste,
			String location, String education, String profession, String mothertongue, Double height, String eating,
			String manglik, String lookingfor, String about, String image, int online) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.bday = bday;
		this.gender = gender;
		this.religion = religion;
		this.caste = caste;
		this.location = location;
		this.education = education;
		this.profession = profession;
		this.mothertongue = mothertongue;
		this.height = height;
		this.eating = eating;
		this.manglik = manglik;
		this.lookingfor = lookingfor;
		this.about = about;
		this.image = image;
		this.online = online;
	}

	public User(int id, String firstname, String lastname, String email, String password, Date bday, String gender, int online) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		this.bday = bday;
		this.gender = gender;
		this.online = online;
	}

	public User(int id, String firstname, String lastname, String email, Date bday, String gender, int online) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.bday = bday;
		this.gender = gender;
		this.online = online;
	}

	public User(int id, String email, String firstname) {
		this.id = id;
		this.email = email;
		this.firstname = firstname;
	}

	public int getAge(Date bday) {
		int userAge;
		Calendar cal = Calendar.getInstance();
		int nowYr = cal.get(Calendar.YEAR);
		cal.setTime(bday);
		userAge = nowYr - cal.get(Calendar.YEAR);
		this.age = userAge;
		return userAge;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public String getCaste() {
		return caste;
	}

	public void setCaste(String caste) {
		this.caste = caste;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getMothertongue() {
		return mothertongue;
	}

	public void setMothertongue(String mothertongue) {
		this.mothertongue = mothertongue;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public String getEating() {
		return eating;
	}

	public void setEating(String eating) {
		this.eating = eating;
	}

	public String getManglik() {
		return manglik;
	}

	public void setManglik(String manglik) {
		this.manglik = manglik;
	}

	public String getLookingfor() {
		return lookingfor;
	}

	public void setLookingfor(String lookingfor) {
		this.lookingfor = lookingfor;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getId() {
		return id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBday() {
		return bday;
	}

	public void setBday(Date bday) {
		this.bday = bday;
	}

	public void setOnline(int online) {
		this.online = online;
	}

	public int getOnline() {
		return online;
	}

	public boolean isOnline() {
		return (getOnline() == 1);
	}
}

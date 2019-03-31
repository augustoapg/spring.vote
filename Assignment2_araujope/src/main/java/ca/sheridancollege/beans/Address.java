/**
 * Name: Augusto Araujo Peres Goncalez
 * Project: Elections
 * Description: This app allows the user to register and vote in one of 5 different parties. It also display a list
 * 				of all the voters registered, enabling the Edit/Delete of each, and some statistics regarding the data collected.
 * 				The app also allows to add 200 dummy voters and a random number of dummy votes.
 * Date: 31 Mar. 2019
 * 
 * File: Address - Java Bean for creating an Address object
 */

package ca.sheridancollege.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Address {

	@Id
	@GeneratedValue
	private int addressId;
	private String street;
	private String city;
	private String province;
	private String postal;
	
	public Address(String street, String city, String province, String postal) {
		this.street = street;
		this.city = city;
		this.province = province;
		this.postal = postal;
	}
}

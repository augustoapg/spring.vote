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

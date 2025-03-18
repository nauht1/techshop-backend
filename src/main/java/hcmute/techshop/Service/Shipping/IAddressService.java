package hcmute.techshop.Service.Shipping;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Model.Shipping.AddressRequestDTO;
import hcmute.techshop.Model.Shipping.AddressResponseDTO;

import java.util.List;

public interface IAddressService {
    List<AddressResponseDTO> getUserAddresses(UserEntity user);
    AddressResponseDTO addAddress(UserEntity user, AddressRequestDTO request);
    AddressResponseDTO updateAddress(UserEntity user, Integer addressId, AddressRequestDTO request);
    void removeAddress(UserEntity user, Integer addressId);
    AddressResponseDTO setDefaultAddress(UserEntity user, Integer addressId);
}
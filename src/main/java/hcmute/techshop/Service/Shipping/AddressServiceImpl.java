package hcmute.techshop.Service.Shipping;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Shipping.AddressEntity;
import hcmute.techshop.Entity.Shipping.ShippingMethodEntity;
import hcmute.techshop.Exception.ResourceNotFoundException;
import hcmute.techshop.Model.Shipping.AddressRequestDTO;
import hcmute.techshop.Model.Shipping.AddressResponseDTO;
import hcmute.techshop.Repository.Shipping.AddressRepository;
import hcmute.techshop.Repository.Shipping.ShippingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements IAddressService {
    private final AddressRepository addressRepository;
    private final ShippingRepository shippingRepository;

    @Override
    public List<AddressResponseDTO> getUserAddresses(UserEntity user) {
        List<AddressEntity> addresses = addressRepository.findByUser(user);
        return addresses.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AddressResponseDTO addAddress(UserEntity user, AddressRequestDTO request) {
        AddressEntity address = new AddressEntity();
        address.setUser(user);
        address.setStreet(request.getStreet());
        address.setWard(request.getWard());
        address.setDistrict(request.getDistrict());
        address.setProvince(request.getProvince());

        // Handle shipping method if provided
//        if (request.getShippingMethodId() != null) {
//            ShippingMethodEntity shippingMethod = shippingRepository.findById(request.getShippingMethodId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Phương thức vận chuyển không tồn tại"));
//            address.setShippingMethod(shippingMethod);
//        }

        // Check if this is the first address for the user
        List<AddressEntity> existingAddresses = addressRepository.findByUser(user);
        boolean isFirstAddress = existingAddresses.isEmpty();

        // If it's the first address or request specifies it as default
        if (isFirstAddress || request.isDefault()) {
            // Set all existing addresses to non-default
            existingAddresses.forEach(existingAddress -> {
                existingAddress.setDefault(false);
                addressRepository.save(existingAddress);
            });

            // Set the new address as default
            address.setDefault(true);
        } else {
            address.setDefault(false);
        }

        AddressEntity savedAddress = addressRepository.save(address);
        return mapToDTO(savedAddress);
    }

    @Override
    @Transactional
    public AddressResponseDTO updateAddress(UserEntity user, Integer addressId, AddressRequestDTO request) {
        AddressEntity address = addressRepository.findByIdAndUser(addressId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Địa chỉ không tồn tại hoặc không thuộc về người dùng này"));

        address.setStreet(request.getStreet());
        address.setWard(request.getWard());
        address.setDistrict(request.getDistrict());
        address.setProvince(request.getProvince());

        // Handle shipping method if provided
//        if (request.getShippingMethodId() != null) {
//            ShippingMethodEntity shippingMethod = shippingRepository.findById(request.getShippingMethodId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Phương thức vận chuyển không tồn tại"));
//            address.setShippingMethod(shippingMethod);
//        } else {
//            address.setShippingMethod(null);
//        }

        // If request wants to set this as default
        if (request.isDefault() && !address.isDefault()) {
            // Set all existing addresses to non-default
            List<AddressEntity> existingAddresses = addressRepository.findByUser(user);
            existingAddresses.forEach(existingAddress -> {
                existingAddress.setDefault(false);
                addressRepository.save(existingAddress);
            });

            // Set this address as default
            address.setDefault(true);
        }

        AddressEntity updatedAddress = addressRepository.save(address);
        return mapToDTO(updatedAddress);
    }

    @Override
    @Transactional
    public void removeAddress(UserEntity user, Integer addressId) {
        AddressEntity address = addressRepository.findByIdAndUser(addressId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Địa chỉ không tồn tại hoặc không thuộc về người dùng này"));

        // If this is the default address, we need to set another address as default (if any)
        if (address.isDefault()) {
            addressRepository.delete(address);

            // Find another address to set as default
            List<AddressEntity> remainingAddresses = addressRepository.findByUser(user);
            if (!remainingAddresses.isEmpty()) {
                AddressEntity newDefaultAddress = remainingAddresses.get(0);
                newDefaultAddress.setDefault(true);
                addressRepository.save(newDefaultAddress);
            }
        } else {
            addressRepository.delete(address);
        }
    }

    @Override
    @Transactional
    public AddressResponseDTO setDefaultAddress(UserEntity user, Integer addressId) {
        AddressEntity newDefaultAddress = addressRepository.findByIdAndUser(addressId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Địa chỉ không tồn tại hoặc không thuộc về người dùng này"));

        // Get current default address if exists
        Optional<AddressEntity> currentDefaultAddress = addressRepository.findByUserAndIsDefaultTrue(user);

        // If current default is different from new default
        if (currentDefaultAddress.isPresent() && !currentDefaultAddress.get().getId().equals(addressId)) {
            AddressEntity current = currentDefaultAddress.get();
            current.setDefault(false);
            addressRepository.save(current);
        }

        // Set new default
        newDefaultAddress.setDefault(true);
        addressRepository.save(newDefaultAddress);

        return mapToDTO(newDefaultAddress);
    }

    private AddressResponseDTO mapToDTO(AddressEntity entity) {
        return AddressResponseDTO.builder()
                .id(entity.getId())
                .street(entity.getStreet())
                .ward(entity.getWard())
                .district(entity.getDistrict())
                .province(entity.getProvince())
                .isDefault(entity.isDefault())
//                .shippingMethodId(entity.getShippingMethod() != null ? entity.getShippingMethod().getId() : null)
//                .shippingMethodName(entity.getShippingMethod() != null ? entity.getShippingMethod().getName() : null)
                .build();
    }
}
package hcmute.techshop.Service.Product.dashboard;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Enum.PaymentStatus;
import hcmute.techshop.Model.Auth.UserModel;
import hcmute.techshop.Model.Dashboard.TopUserDTO;
import hcmute.techshop.Repository.Auth.UserRepository;
import hcmute.techshop.Service.User.IUserService;
import hcmute.techshop.Service.User.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements IDashboardService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<TopUserDTO> getTopUsers() {
        Pageable pageable = PageRequest.of(0, 10);

        List<Object[]> result = userRepository.findTopUser(PaymentStatus.SUCCESS, pageable);

        return result.stream()
                .map(obj -> {
                    UserEntity user = (UserEntity) obj[0];
                    Double amountSpent = (Double) obj[1];
                    UserModel userModel = modelMapper.map(user, UserModel.class);
                    return new TopUserDTO(userModel, amountSpent);
                })
                .toList();
    }
}

package hcmute.techshop.Service.Product.dashboard;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Enum.PaymentStatus;
import hcmute.techshop.Model.Auth.UserModel;
import hcmute.techshop.Model.Dashboard.TopUserDTO;
import hcmute.techshop.Model.PageResponse;
import hcmute.techshop.Repository.Auth.UserRepository;
import hcmute.techshop.Service.User.IUserService;
import hcmute.techshop.Service.User.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements IDashboardService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public PageResponse<TopUserDTO> getTopUsers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "totalAmount"));

        Page<Object[]> pageResult = userRepository.findTopUser(PaymentStatus.SUCCESS, pageable);

        List<TopUserDTO> userDTOS = pageResult.stream()
                .map(obj -> {
                    UserEntity user = (UserEntity) obj[0];
                    Double amountSpent = (Double) obj[1];
                    UserModel userModel = modelMapper.map(user, UserModel.class);
                    return new TopUserDTO(userModel, amountSpent);
                })
                .toList();

        return new PageResponse<>(userDTOS, pageResult.getNumber(), pageResult.getTotalPages());
    }
}

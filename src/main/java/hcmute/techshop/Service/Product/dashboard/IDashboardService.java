package hcmute.techshop.Service.Product.dashboard;

import hcmute.techshop.Model.Auth.UserModel;
import hcmute.techshop.Model.Dashboard.TopUserDTO;

import java.util.List;

public interface IDashboardService {
    List<TopUserDTO> getTopUsers();
}

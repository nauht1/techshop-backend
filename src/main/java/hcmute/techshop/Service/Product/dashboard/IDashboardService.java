package hcmute.techshop.Service.Product.dashboard;

import hcmute.techshop.Model.Auth.UserModel;
import hcmute.techshop.Model.Dashboard.TopUserDTO;
import hcmute.techshop.Model.PageResponse;

import java.util.List;

public interface IDashboardService {
    PageResponse<TopUserDTO> getTopUsers(int pageNumber, int pageSize);
}

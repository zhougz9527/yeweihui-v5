function getProvinceList(vue) {
    const self = vue;
    $.ajax({
        type: "GET",
        url: baseURL + "division/province/all",
        success: function(r){
            if(r.code === 0){
                self.provinceList = r.all
            }else{
                console.log(r.msg);
            }
        }
    });
}

function getCityList(vue, provinceId) {
    const self = vue;
    if (provinceId) {
        $.ajax({
            type: "GET",
            url: baseURL + `division/city/citiesInProvince?provinceId=${provinceId}`,
            success: function(r){
                if(r.code === 0){
                    self.cityList = r.cities;
                }else{
                    console.log(r.msg);
                }
            }
        });
    } else {
        $.ajax({
            type: "GET",
            url: baseURL + "division/city/all",
            success: function(r){
                if(r.code === 0){
                    self.cityList = r.all
                }else{
                    console.log(r.msg);
                }
            }
        });
    }
}

function getDistrictList(vue, cityId) {
    const self = vue;
    if (cityId) {
        $.ajax({
            type: "GET",
            url: baseURL + `division/district/districtsInCity?cityId=${cityId}`,
            success: function(r){
                if(r.code === 0){
                    self.districtList = r.districts;
                }else{
                    console.log(r.msg);
                }
            }
        });
    } else {
        $.ajax({
            type: "GET",
            url: baseURL + "division/district/all",
            success: function(r){
                if(r.code === 0){
                    self.districtList = r.all
                }else{
                    console.log(r.msg);
                }
            }
        });
    }
}

function getSubdistrictList(vue, districtId) {
    const self = vue;
    if (districtId) {
        $.ajax({
            type: "GET",
            url: baseURL + `division/subdistrict/subdistrictsInDistrict?districtId=${districtId}`,
            success: function(r){
                if(r.code === 0){
                    self.subdistrictList = r.subdistricts;
                }else{
                    console.log(r.msg);
                }
            }
        });
    } else {
        $.ajax({
            type: "GET",
            url: baseURL + "division/subdistrict/all",
            success: function(r){
                if(r.code === 0){
                    self.subdistrictList = r.all
                }else{
                    console.log(r.msg);
                }
            }
        });
    }
}

function getCommunityList(vue, subdistrictId) {
    const self = vue;
    if (subdistrictId) {
        $.ajax({
            type: "GET",
            url: baseURL + `division/community/communitiesInSubdistrict?subdistrictId=${subdistrictId}`,
            success: function(r){
                if(r.code === 0){
                    self.communityList = r.communities;
                }else{
                    console.log(r.msg);
                }
            }
        });
    } else {
        $.ajax({
            type: "GET",
            url: baseURL + "division/community/all",
            success: function(r){
                if(r.code === 0){
                    self.communityList = r.all
                }else{
                    console.log(r.msg);
                }
            }
        });
    }
}

function getUserListByRoleName(vue, roleName) {
    const self = vue;
    $.ajax({
        type: "GET",
        url: baseURL + `user/user/roleUser?roleName=${roleName}`,
        success: function(r){
            if(r.code === 0){
                self.userList = r.users;
            }else{
                alert(r.msg);
            }
        }
    });
}

export default {
    getProvinceList,
    getCityList,
    getDistrictList,
    getSubdistrictList,
    getCommunityList,
    getUserListByRoleName
}
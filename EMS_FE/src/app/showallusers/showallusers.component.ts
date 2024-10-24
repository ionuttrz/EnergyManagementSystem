import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Device } from '../device';

@Component({
  selector: 'app-showallusers',
  templateUrl: './showallusers.component.html',
  styleUrls: ['./showallusers.component.css']
})
export class ShowallusersComponent implements OnInit {

  public users: User[] = [];

  public devices: Device[] = [];

  selectedUser: User | null = null;

  editUser: boolean = false;

  selectedDevice: Device | null = null;

  editDevice: boolean = false;

  showDevicesContainer: boolean = false;


  constructor(private http: HttpClient) { }

  fetchData() {
    const httpOptions = {
      headers: new HttpHeaders({
        'content-type': 'application/json',
        'authorization': `Bearer ${localStorage.getItem('accessToken')}`
      })
    };
    this.http.get<any>(`http://localhost:8090/user/showallusers`, httpOptions).subscribe(
      data => {
        this.users = data;
        console.log(data);
        console.log(this.users[0]);
      },
      error => {
        console.log(error);
      }
    );
  }
  
  ngOnInit() {
    this.fetchData();
  }

  getRole(userRoles: string): string {
    const matches = userRoles.match(/role=([\w-]+)/);
    return matches ? matches[1] : "N/A";
  }
  

  deleteUser(user: User) {
    const userEmail = user.email;
    const httpOptions = {
      headers: new HttpHeaders({
        'content-type': 'application/json',
        'authorization': `Bearer ${localStorage.getItem('accessToken')}`
      })
    };
    this.http
    .delete(`http://localhost:8090/user/deleteuser?userEmail=${userEmail}`, httpOptions)
      .subscribe(
        (data:any) => {
          this.http.delete(`http://localhost:8091/device/deletedevicebymail?userEmail=${userEmail}`, httpOptions)
            .subscribe(
              (data:any) => {
              },
              (error) => {
                console.log(error.message);
                alert("Error deleting device");
              }
            );
          alert("User and their devices deleted successfully.");
          this.fetchData();
        },
        (error) => {
          console.log(error.message);
          alert("Error deleting user");
        }
      );
  }

  modifyUser(user: User) {
    this.editUser = true;
    this.selectedUser = user;
  }

  saveUserChanges(user: User) {
    this.http.put(`http://localhost:8090/user/modifyuser`, user)
      .subscribe(
        (data: any) =>{
          console.log(data);
          alert("User modified successfully.");
        },
        (error) =>{
          console.log(error);
          alert("Error modifying device");
        }
      );
    this.editUser = false;
  }

  selectUser(user:User){
    this.selectedUser = user;
  }

  viewUserDevices(user: User) {
    this.showDevicesContainer = true;
    const userEmail = user.email;
    this.http.get<any>(`http://localhost:8091/device/showalldevices?userEmail=${userEmail}`).subscribe(
      data => {
        this.devices = data;
        console.log(data);
        console.log(this.devices[0]);
      },
      error => {
        console.log(error);
      }
    );
  }

  modifyDevice(device: Device) {
    this.editDevice = true;
    this.selectedDevice = device;
  }

  saveDeviceChanges(device: Device) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      })
    };
    this.http.put(`http://localhost:8091/device/modifydevice`, device, httpOptions)
      .subscribe(
        (data: any) =>{
          console.log(data);
          alert("Device modified successfully.");
        },
        (error) =>{
          console.log(error);
          alert("Error modifying device");
        }
      );
    this.editDevice = false;
  }

  deleteDevice(device: Device) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      })
    };
    const deviceId = device.id;
    this.http
    .delete(`http://localhost:8091/device/deletedevice?deviceId=${deviceId}`, httpOptions)
      .subscribe(
        (data:any) => {
          console.log("Device deleted!");
          alert("Device deleted successfully.");
          if(this.selectedUser != null){
            this.viewUserDevices(this.selectedUser);
          }
        },
        (error) => {
          console.log(error.message);
          //alert("Error deleting device");
        }
      );
  }

}

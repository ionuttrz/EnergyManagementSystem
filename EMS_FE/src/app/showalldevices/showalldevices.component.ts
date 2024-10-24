import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Device } from '../device';
import { JwtHelperService } from '@auth0/angular-jwt';
import { DevicesService } from '../devices.service';

@Component({
  selector: 'app-showalldevices',
  templateUrl: './showalldevices.component.html',
  styleUrls: ['./showalldevices.component.css']
})
export class ShowalldevicesComponent implements OnInit {
  public devices: Device[] = [];
  showChart: boolean = false;

  constructor(private http: HttpClient, private deviceService: DevicesService) { }

  showDeviceChart(deviceId: number){
    this.deviceService.setSelectedDeviceId(deviceId);
    this.showChart = true;
  }

  fetchData() {
    const jwtHelper = new JwtHelperService();
    const token = localStorage.getItem('accessToken');
    let userEmail: string | null = null;
    if (token) {
      userEmail = jwtHelper.decodeToken(token).sub;
      console.log(userEmail);
    } else {
      console.error('Access token is not available in localStorage.');
    }
    console.log(userEmail);
    console.log(localStorage.getItem('accessToken'));
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

  ngOnInit() {
    this.fetchData();
  }

}

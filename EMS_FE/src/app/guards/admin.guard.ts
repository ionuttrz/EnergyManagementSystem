import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(): boolean {

    const userRole = localStorage.getItem("userRole");

    if (userRole === "[ADMIN]") {
      return true;
    } else {
      alert('Permission denied. You do not have access to admin page.');
      this.router.navigate(['/client-page']);
      return false;
    }
  }
  
}

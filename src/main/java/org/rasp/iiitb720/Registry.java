package org.rasp.iiitb720;
import platform.helper.HelperManager;
import platform.webservice.ServiceManager;
import org.rasp.iiitb720.helper.*;
import org.rasp.iiitb720.service.*;
public class Registry {
		public static void register(){
				 HelperManager.getInstance().register(AvailabilityHelper.getInstance());
				 HelperManager.getInstance().register(NotificationHelper.getInstance());
				 HelperManager.getInstance().register(PaymentHelper.getInstance());
				 HelperManager.getInstance().register(AuthHelper.getInstance());
				 HelperManager.getInstance().register(ReservationHelper.getInstance());
				 HelperManager.getInstance().register(InterfaceHelper.getInstance());
				 ServiceManager.getInstance().register(new AvailabilityService());
				 ServiceManager.getInstance().register(new NotificationService());
				 ServiceManager.getInstance().register(new PaymentService());
				 ServiceManager.getInstance().register(new AuthService());
				 ServiceManager.getInstance().register(new ReservationService());
				 ServiceManager.getInstance().register(new InterfaceService());
		}
}

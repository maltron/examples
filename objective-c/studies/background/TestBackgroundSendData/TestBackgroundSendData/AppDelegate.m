//
//  AppDelegate.m
//  TestBackgroundSendData
//
//  Created by Mauricio Leal on 2/7/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "AppDelegate.h"
#import "PersonViewController.h"
#import "Person.h"

@interface AppDelegate ()
@property (nonatomic, strong) UIAlertView *alertView;
@property (readonly, nonatomic, strong) NSManagedObjectContext *contextToServer;

@end

@implementation AppDelegate
@synthesize alertView = _alertView;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    NSLog(@">>> [AppDelegate didFinishLaunchingWithOptions:launchOptions]");
    
    _window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    PersonViewController *viewController = [[PersonViewController alloc] init];
    UINavigationController *nav = [[UINavigationController alloc] initWithRootViewController:viewController];
    [self.window setRootViewController:nav];
    [self.window makeKeyAndVisible];
    
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application {
    NSLog(@">>> [AppDelegate applicationWillResignActive]");
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    NSLog(@">>> [AppDelegate applicationDidEnterBackground]");
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    NSLog(@">>> [AppDelegate applicationWillEnterForeground]");
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    NSLog(@">>> [AppDelegate applicationDidBecomeActive]");
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application {
    NSLog(@">>> [AppDelegate applicationWillTerminate]");
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    // Saves changes in the application's managed object context before the application terminates.
    [self saveContext];
}

#pragma mark - Core Data stack

@synthesize managedObjectContext = _managedObjectContext;
@synthesize managedObjectModel = _managedObjectModel;
@synthesize persistentStoreCoordinator = _persistentStoreCoordinator;
@synthesize contextToServer = _contextToServer;

-(void)sendToServer {
    NSLog(@"[AppDelegate sendToServer]");
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    [fetchRequest setEntity:[self entityPerson]];
    
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"status=%@", NO];
    [fetchRequest setPredicate:predicate];
    
    NSError *error = nil;
    NSArray *results = [self.contextToServer executeFetchRequest:fetchRequest error:&error];
    if(error) {
        NSLog(@"### [AppDelegate sendtoServer] Error:%@, %@", error, [error userInfo]);
        abort();
    }
    
    if(results && [results count] > 0) {
        NSLog(@"[AppDelegate sendToServer] FOUND Person with Status = NO");
        
        
    }
    
    
}

+(AppDelegate *)instance {
    return [[UIApplication sharedApplication] delegate];
}

-(UIAlertView *)alertTitle:(NSString *)title withMessage:(NSString *)message {
    if(!_alertView) _alertView = [[UIAlertView alloc] initWithTitle:title message:message delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles: nil];
    
    [_alertView setTitle:title];
    [_alertView setMessage:message];
    [_alertView setAlertViewStyle:UIAlertViewStyleDefault];
    
    return _alertView;
}

-(void)alertAsyncTitle:(NSString *)title withMessage:(NSString *)message {
    if(!_alertView) _alertView = [[UIAlertView alloc] initWithTitle:title message:message delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
    
    [_alertView setTitle:title];
    [_alertView setMessage:message];
    [_alertView setAlertViewStyle:UIAlertViewStyleDefault];
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [_alertView show];
    });
}

-(void)alertNetworkProblem {
    dispatch_async(dispatch_get_main_queue(), ^{
        [[self alertTitle:@"Network Problem"
             withMessage:@"Unable to comunicate with the Server"] show];
    });
}

-(void)alertServerProblem {
    dispatch_async(dispatch_get_main_queue(), ^{
        [[self alertTitle:@"Server Problem"
             withMessage:@"Unable to perform operation"] show];
    });
    
}

-(void)alertAsyncJSONProblem {
    dispatch_async(dispatch_get_main_queue(), ^{
        [[self alertTitle:@"Parsing Problem"
              withMessage:@"Unable to understand message from Server"] show];
    });
}

-(NSEntityDescription *)entityPerson {
    return [NSEntityDescription entityForName:@"Person" inManagedObjectContext:[self managedObjectContext]];
}

-(Person *)newPerson {
    return (Person *)[[NSManagedObject alloc] initWithEntity:[self entityPerson] insertIntoManagedObjectContext:[self managedObjectContext]];
}

- (NSURL *)applicationDocumentsDirectory {
    // The directory the application uses to store the Core Data store file. This code uses a directory named "net.nortlam.TestBackgroundSendData" in the application's documents directory.
    return [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject];
}

- (NSManagedObjectModel *)managedObjectModel {
    // The managed object model for the application. It is a fatal error for the application not to be able to find and load its model.
    if (_managedObjectModel != nil) {
        return _managedObjectModel;
    }
    NSURL *modelURL = [[NSBundle mainBundle] URLForResource:@"Person" withExtension:@"momd"];
    _managedObjectModel = [[NSManagedObjectModel alloc] initWithContentsOfURL:modelURL];
    return _managedObjectModel;
}

- (NSPersistentStoreCoordinator *)persistentStoreCoordinator {
    // The persistent store coordinator for the application. This implementation creates and return a coordinator, having added the store for the application to it.
    if (_persistentStoreCoordinator != nil) {
        return _persistentStoreCoordinator;
    }
    
    // Create the coordinator and store
    
    _persistentStoreCoordinator = [[NSPersistentStoreCoordinator alloc] initWithManagedObjectModel:[self managedObjectModel]];
    NSURL *storeURL = [[self applicationDocumentsDirectory] URLByAppendingPathComponent:@"Person"];
    NSError *error = nil;
    NSString *failureReason = @"There was an error creating or loading the application's saved data.";
    if (![_persistentStoreCoordinator addPersistentStoreWithType:NSSQLiteStoreType configuration:nil URL:storeURL options:nil error:&error]) {
        // Report any error we got.
        NSMutableDictionary *dict = [NSMutableDictionary dictionary];
        dict[NSLocalizedDescriptionKey] = @"Failed to initialize the application's saved data";
        dict[NSLocalizedFailureReasonErrorKey] = failureReason;
        dict[NSUnderlyingErrorKey] = error;
        error = [NSError errorWithDomain:@"YOUR_ERROR_DOMAIN" code:9999 userInfo:dict];
        // Replace this with code to handle the error appropriately.
        // abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
        NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
        abort();
    }
    
    return _persistentStoreCoordinator;
}


- (NSManagedObjectContext *)managedObjectContext {
    // Returns the managed object context for the application (which is already bound to the persistent store coordinator for the application.)
    if (_managedObjectContext != nil) {
        return _managedObjectContext;
    }
    
    NSPersistentStoreCoordinator *coordinator = [self persistentStoreCoordinator];
    if (!coordinator) {
        return nil;
    }
    _managedObjectContext = [[NSManagedObjectContext alloc] init];
    [_managedObjectContext setPersistentStoreCoordinator:coordinator];
    return _managedObjectContext;
}

-(NSManagedObjectContext *)contextToServer {
    if(_contextToServer) return _contextToServer;
    
    NSPersistentStoreCoordinator *coordinator = [self persistentStoreCoordinator];
    if(!coordinator) return nil;
    
    // Create another context in a different thread
    _contextToServer = [[NSManagedObjectContext alloc] initWithConcurrencyType:NSPrivateQueueConcurrencyType];
    [_contextToServer setPersistentStoreCoordinator:coordinator];
    
    return _contextToServer;
}

#pragma mark - Core Data Saving support

- (void)saveContext {
    NSManagedObjectContext *managedObjectContext = self.managedObjectContext;
    if (managedObjectContext != nil) {
        NSError *error = nil;
        if ([managedObjectContext hasChanges] && ![managedObjectContext save:&error]) {
            // Replace this implementation with code to handle the error appropriately.
            // abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
            NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
            abort();
        }
    }
}

#pragma mark - URL SESSION

@synthesize urlSessionConfiguration = _urlSessionConfiguration;
@synthesize urlSession = _urlSession;

-(NSURL *)url {
    return [NSURL URLWithString:SERVER];
}

-(NSURL *)urlFindFirst:(NSString *)firstName andLastName:(NSString *)lastName {
    NSString *find = [NSString stringWithFormat:@"/find/%@/%@", [firstName stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding],
                      [lastName stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    return [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", SERVER, find]];
}

-(NSURL *)urlDelete:(Person *)person {
    NSString *delete = [NSString stringWithFormat:@"/%@", [person iD]];
    return [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", SERVER, delete]];
}

-(NSURLSessionConfiguration *)urlSessionConfiguration {
    if(_urlSessionConfiguration) return _urlSessionConfiguration;
    
    // Possible configurations: defaultSessionConfiguration, ephemeralSessionConfiguration,
    //                          backgroundSessionConfigurationWithIdentifier:
    _urlSessionConfiguration = [NSURLSessionConfiguration ephemeralSessionConfiguration];
    // PENDING: Further Configurations (like being able to work only in WiFi)
    _urlSessionConfiguration.HTTPAdditionalHeaders = @{@"Content-type":@"application/json",
                                                       @"Accept":@"application/json"
                                                       };
    
    return _urlSessionConfiguration;
}

-(NSURLSession *)urlSession {
    if(_urlSession) return _urlSession;
    
    _urlSession = [NSURLSession sessionWithConfiguration:[self urlSessionConfiguration]];
    // PENDING: Further Session Configurations
    return _urlSession;
}

@end

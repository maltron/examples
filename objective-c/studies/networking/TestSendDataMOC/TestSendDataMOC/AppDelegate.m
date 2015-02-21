//
//  AppDelegate.m
//  TestSendDataMOC
//
//  Created by Mauricio Leal on 2/16/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "AppDelegate.h"
#import "PersonViewController.h"

@interface AppDelegate ()
@property (readonly, strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property (readonly, strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;
@property (nonatomic, strong) UIAlertView *alertView;
@property (readonly, nonatomic, strong) NSURLSessionConfiguration *sessionConfiguration;
@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    NSLog(@">>> [AppDelegate didFinishLaunchingWithOptions]");
    _window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    
    PersonCRUD *crud = [[PersonCRUD alloc] initWithContext:[self mainContext]];
    PersonViewController *viewController = [[PersonViewController alloc] initWithCRUD:crud];
    UINavigationController *navigation = [[UINavigationController alloc] initWithRootViewController:viewController];
    [self.window setRootViewController:navigation];
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
    NSLog(@">>> [AppDelegate applicationWIllEnterForeground]");
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
    [self saveMainContext];
}

#pragma mark - Core Data stack

@synthesize mainContext = _mainContext, backgroundContext = _backgroundContext;
@synthesize managedObjectModel = _managedObjectModel;
@synthesize persistentStoreCoordinator = _persistentStoreCoordinator;
@synthesize alertView = _alertView;
@synthesize sessionConfiguration = _sessionConfiguration;
@synthesize session = _session;

+(AppDelegate *)instance {
    return [[UIApplication sharedApplication] delegate];
}

-(UIAlertView *)alertTitle:(NSString *)title withMessage:(NSString *)message {
    if(!_alertView) _alertView = [[UIAlertView alloc] initWithTitle:title message:message delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
    
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

-(void)alertAsyncNetworkProblem {
    dispatch_async(dispatch_get_main_queue(), ^{
        [[self alertTitle:@"Network Problem"
              withMessage:@"Unable to communicate with the Server"] show];
    });
}

-(void)alertAsyncServerProblem {
    dispatch_async(dispatch_get_main_queue(), ^{
        [[self alertTitle:@"Server Problem"
              withMessage:@"Unable to process Server's response"] show];
    });
}

-(void)alertAsyncJSONProblem {
    dispatch_async(dispatch_get_main_queue(), ^{
        [[self alertTitle:@"Parsing Problem"
              withMessage:@"Unable to understand message from Server"] show];
    });
}

-(void)alertAsyncCoreDataProblem {
    dispatch_async(dispatch_get_main_queue(), ^{
        [[self alertTitle:@"Data Problem"
              withMessage:@"Unable perform data operation"] show];
    });
}

-(NSEntityDescription *)entityPerson {
    return [NSEntityDescription entityForName:@"Person" inManagedObjectContext:[self mainContext]];
}

-(Person *)newPerson {
    return (Person *)[[NSManagedObject alloc] initWithEntity:[self entityPerson] insertIntoManagedObjectContext:[self mainContext]];
}


- (NSURL *)applicationDocumentsDirectory {
    // The directory the application uses to store the Core Data store file. This code uses a directory named "net.nortlam.TestSendDataMOC" in the application's documents directory.
    return [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject];
}

- (NSManagedObjectModel *)managedObjectModel {
    // The managed object model for the application. It is a fatal error for the application not to be able to find and load its model.
    if (_managedObjectModel != nil) {
        return _managedObjectModel;
    }
    NSURL *modelURL = [[NSBundle mainBundle] URLForResource:@"DatabaseModel" withExtension:@"momd"];
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
    NSURL *storeURL = [[self applicationDocumentsDirectory] URLByAppendingPathComponent:@"DatabaseModel.sqlite"];
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

-(NSManagedObjectContext *)mainContext {
    NSLog(@"[AppDelegate mainContext]");
    if(_mainContext) return _mainContext;
    
//    NSLog(@"[AppDelegate mainContext] Getting Coordinator");
//    NSPersistentStoreCoordinator *coordinator = [self persistentStoreCoordinator];
//    if(!coordinator) {
//        NSLog(@"### [AppDelegate mainContext] WARNING: Store Coordinator is NIL");
//        return nil;
//    }
    
    NSLog(@"[AppDelegate mainContext] Constructing Context");
    _mainContext = [[NSManagedObjectContext alloc] initWithConcurrencyType:NSMainQueueConcurrencyType];
//    NSLog(@"[AppDelegate mainContext] Setting Coordinator");
//    [_mainContext setPersistentStoreCoordinator:coordinator];
    NSLog(@"[AppDelegate mainContext] Setting Parent: backgroundContext");
    [_mainContext setParentContext:[self backgroundContext]];
    
    NSLog(@"[AppDelegate mainContext] Returning instance");
    return _mainContext;
}

-(NSManagedObjectContext *)backgroundContext {
    NSLog(@"[AppDelegate backgroundContext]");
    if(_backgroundContext) return _backgroundContext;
    
    NSLog(@"[AppDelegate backgroundContext] Getting Coordinator");
    NSPersistentStoreCoordinator *coordinator = [self persistentStoreCoordinator];
    if(!coordinator) {
        NSLog(@"### [AppDelegate backgroundContext] WARNING: Store Coordinator is NIL");
        return nil;
    }
    
    NSLog(@"[AppDelegate backgroundContext] Constructing Context");
    _backgroundContext = [[NSManagedObjectContext alloc] initWithConcurrencyType:NSPrivateQueueConcurrencyType];
    NSLog(@"[AppDelegate backgroundContext] Setting Coordinator");
    [_backgroundContext setPersistentStoreCoordinator:coordinator];
    
    NSLog(@"[AppDelegate backgroundContext] Returning instance");
    return _backgroundContext;
}

//- (NSManagedObjectContext *)managedObjectContext {
//    // Returns the managed object context for the application (which is already bound to the persistent store coordinator for the application.)
//    if (_managedObjectContext != nil) {
//        return _managedObjectContext;
//    }
//    
//    NSPersistentStoreCoordinator *coordinator = [self persistentStoreCoordinator];
//    if (!coordinator) {
//        return nil;
//    }
//    _managedObjectContext = [[NSManagedObjectContext alloc] init];
//    [_managedObjectContext setPersistentStoreCoordinator:coordinator];
//    return _managedObjectContext;
//}

#pragma mark - Core Data Saving support

-(void)saveMainContext {
    NSManagedObjectContext *managedObjectContext = [self mainContext];
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

//-(void)saveBackgroundContext {
//    NSManagedObjectContext *context = [self backgroundContext];
//    if(!context) return;
//    
//    NSError *error = nil;
//    if([context hasChanges] && ![context save:&error]) {
//        NSLog(@"### [AppDelegate saveBackgroundContext] Error:%@, %@", error, [error userInfo]);
//        [self alertAsyncCoreDataProblem];
//        abort();
//    }
//}

-(NSURLSession *)session {
    if(_session) return _session;
    
    _session = [NSURLSession sessionWithConfiguration:[self sessionConfiguration]];
    return _session;
}

-(NSURLSessionConfiguration *)sessionConfiguration {
    if(_sessionConfiguration) return _sessionConfiguration;
    
    _sessionConfiguration = [NSURLSessionConfiguration ephemeralSessionConfiguration];
    [_sessionConfiguration setHTTPAdditionalHeaders:@{@"Content-type":@"application/json",
                                                      @"Accept":@"application/json"}];
    
    return _sessionConfiguration;
}

-(NSURL *)url {
    return [NSURL URLWithString:SERVER];
}

-(NSURL *)urlFindFirst:(NSString *)firstName andLastName:(NSString *)lastName {
    NSString *find = [NSString stringWithFormat:@"find/%@/%@",
        [firstName stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding],
        [lastName stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    
    return [NSURL URLWithString:find relativeToURL:[self url]];
}

-(NSURL *)urlDelete:(NSNumber *)iD {
    NSString *delete = [NSString stringWithFormat:@"%@", iD];
    return [NSURL URLWithString:delete relativeToURL:[self url]];
}


@end

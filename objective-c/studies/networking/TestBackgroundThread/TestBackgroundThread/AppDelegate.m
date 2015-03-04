//
//  AppDelegate.m
//  TestBackgroundThread
//
//  Created by Mauricio Leal on 2/23/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "AppDelegate.h"
#import "PersonViewController.h"

@interface AppDelegate ()
@property (readonly, nonatomic, strong) UIAlertView *alertView;
@end

@implementation AppDelegate
@synthesize mainContext = _mainContext;
@synthesize alertView = _alertView;
@synthesize session = _session;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    NSLog(@">>> [AppDelegate didFinishLaunchingWithOptions]");
    _window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    PersonViewController *viewController = [[PersonViewController alloc] init];
    UINavigationController *navigation = [[UINavigationController alloc] initWithRootViewController:viewController];
    [self.window setRootViewController:navigation];
    [self.window makeKeyAndVisible];
    
    // Override point for customization after application launch.
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

+(AppDelegate *)instance {
    return [[UIApplication sharedApplication] delegate];
}

#pragma mark - Core Data stack

-(UIAlertView *)alertViewTitle:(NSString *)title withMessage:(NSString *)message {
    if(!_alertView) _alertView = [[UIAlertView alloc] initWithTitle:title message:message delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
    
    [_alertView setTitle:title];
    [_alertView setMessage:message];
    
    return _alertView;
}

-(void)alertAsyncNetworkProblem {
    dispatch_async(dispatch_get_main_queue(), ^{
        [[self alertViewTitle:@"Network Problem"
                  withMessage:@"Unable to contact Server"] show];
    });
}

-(void)alertAsyncServerProblem {
    dispatch_async(dispatch_get_main_queue(), ^{
        [[self alertViewTitle:@"Server Problem"
                  withMessage:@"Unable to understand Server's Response"] show];
    });
}

-(void)alertAsyncJSONProblem {
    dispatch_async(dispatch_get_main_queue(), ^{
        [[self alertViewTitle:@"JSON Problem"
                  withMessage:@"Unable to process Server's Response"] show];
    });
}

-(NSManagedObjectContext *)mainContext {
    if(_mainContext) return _mainContext;
    
    // Model
    NSURL *modelURL = [[NSBundle mainBundle] URLForResource:@"DatabaseModel" withExtension:@"momd"];
    NSManagedObjectModel *model = [[NSManagedObjectModel alloc] initWithContentsOfURL:modelURL];
    
    NSPersistentStoreCoordinator *coordinator = [[NSPersistentStoreCoordinator alloc] initWithManagedObjectModel:model];
    
    NSURL *storeURL = [[[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject] URLByAppendingPathComponent:@"DatabaseModel.sqlite"];
    NSError *error = nil;
    NSString *failurReason = @"There was an error creating or loading the application's saved data";
    if(![coordinator addPersistentStoreWithType:NSSQLiteStoreType configuration:nil URL:storeURL options:nil error:&error]) {
        // Report an error we got
        NSMutableDictionary *dict = [NSMutableDictionary dictionary];
        dict[NSLocalizedDescriptionKey] = @"Failed to initialize the application's saved data";
        dict[NSLocalizedFailureReasonErrorKey] = failurReason;
        dict[NSUnderlyingErrorKey] = error;
        
        error = [NSError errorWithDomain:@"YOUR_ERROR_DOMAIN" code:9999 userInfo:dict];
        // Replace this with code to handle the error appropriately
        // abort() causes the application to generate a crash log and terminate.
        // You should not use this function in a shipping application, although it may be
        // useful during development.
        NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
        abort();
    }
    
    _mainContext = [[NSManagedObjectContext alloc]
                    initWithConcurrencyType:NSMainQueueConcurrencyType];
    [_mainContext setPersistentStoreCoordinator:coordinator];
    error = nil;
    if([_mainContext hasChanges] && ![_mainContext save:&error]) {
        // Replace this implementation with code to handle the error appropriately
        // abort() causes the application to generate a crash log and terminate. You
        // should not use this funcition in a shipping application, although it may
        // be useful during development.
        NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
        abort();
    }
    
    return _mainContext;
}

#pragma mark - Core Data Saving support

//-(void)saveContext:(NSNotification *)notification {
//    NSLog(@">>> [AppDelegate saveContext]");
//
//    if([NSThread mainThread]) {
//        NSLog(@">>> [AppDelegate saveContext] NSThread mainThread");
//        [self.mainContext mergeChangesFromContextDidSaveNotification:notification];
//
//    } else {
//        dispatch_async(dispatch_get_main_queue(), ^{
//           NSLog(@">>> [AppDelegate saveContext] dispatch_async(dispatch_get_main_queue()");
//           [self.mainContext mergeChangesFromContextDidSaveNotification:notification];
//        });
//    }
//
//}


- (void)saveContext {
    NSLog(@">>> [AppDelegate saveContext]");
    NSError *error = nil;
    if ([self.mainContext hasChanges] && ![self.mainContext save:&error]) {
        // Replace this implementation with code to handle the error appropriately.
        // abort() causes the application to generate a crash log and terminate. You should not use
        // this function in a shipping application, although it may be useful during development.
        NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
        abort();
    }
}

-(NSURLSession *)session {
    if(_session) return _session;
    
    NSURLSessionConfiguration *configuration = [NSURLSessionConfiguration ephemeralSessionConfiguration];
    [configuration setHTTPAdditionalHeaders:@{@"Content-type":@"application/json",
                                              @"Accept":@"application/json"}];
    [configuration setTimeoutIntervalForRequest:30];
    [configuration setTimeoutIntervalForResource:30];
    
    _session = [NSURLSession sessionWithConfiguration:configuration];
    return _session;
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

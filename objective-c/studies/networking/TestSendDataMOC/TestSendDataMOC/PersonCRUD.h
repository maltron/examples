//
//  PersonCRUD.h
//  TestSendDataMOC
//
//  Created by Mauricio Leal on 2/16/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Person.h"

@interface PersonCRUD : NSObject
@property (nonatomic, strong) NSNumber *iD;

-(id)initWithContext:(NSManagedObjectContext *)context;

-(void)save:(NSNumber *)iD firstName:(NSString *)firstName andLastName:(NSString *)lastName completionHandler:(void(^)(NSData *data, NSHTTPURLResponse *response, NSError *error))completionHandler;
-(void)remove:(Person *)person completionHandler:(void(^)(NSData *data, NSHTTPURLResponse *response, NSError *error))completionHandler;

-(void)findByFirst:(NSString *)firstName andLastName:(NSString *)lastName completionHandler:(void(^)(NSData *data, NSHTTPURLResponse *r, NSError *error))completionHandler;

-(void)saveContext:(NSNotification *)notification;

@end
